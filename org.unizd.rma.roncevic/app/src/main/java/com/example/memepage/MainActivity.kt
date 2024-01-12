package com.example.memepage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.example.memepage.ui.theme.MemePageTheme
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    private val gifUrls = mutableListOf<String>()
    private val nameOfGIFs = mutableListOf<String>()
    private val gifsDetails = mutableListOf<Array<String>>()

    data class UserDetails(
        val avatarUrl: String,
        val bannerImage: String,
        val bannerUrl: String,
        val profileUrl: String,
        val username: String,
        val displayName: String,
        val description: String,
        val instagramUrl: String,
        val websiteUrl: String,
        val isVerified: Boolean
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createStarterGif()
        hideTextView()


        val buttonCampfire: Button = findViewById(R.id.button2)
        val searchBar: EditText = findViewById(R.id.search_bar)


        activateGifIconForDetails()



        buttonCampfire.setOnClickListener {
            val intent = Intent(this, RestSoldierActivity::class.java)
            startActivity(intent)
        }

        searchBar.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                // Check if touch was inside the drawable area
                if (event.rawX >= searchBar.right - searchBar.compoundDrawables[2].bounds.width()) {
                    // Handle icon click here
                    onSearchIconClick()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

    }

    private fun onSearchIconClick() {

        val searchText = findViewById<EditText>(R.id.search_bar).text.toString()
        if (searchText != "") {
            fetchGif(searchText)
        }


    }

    private fun fetchGif(searchText: String) {
        val apiKey = "API KEY"
        val baseUrl = "https://api.giphy.com/v1/gifs/search"
        val query = searchText.replace(" ", "+")
        val apiUrl = "$baseUrl?api_key=$apiKey&q=$query&limit=1"

        val thread = Thread {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                runOnUiThread {
                    // Process JSON response and extract the GIF URL
                    val jsonResponse = JSONObject(response.toString())
                    val dataArray = jsonResponse.getJSONArray("data")

                    if (dataArray.length() > 0) {
                        val gifUrl = dataArray
                            .getJSONObject(0)
                            .getJSONObject("images")
                            .getJSONObject("downsized")
                            .getString("url")

                        val gifName = dataArray
                            .getJSONObject(0)
                            .getString("title")

                        val userObject = dataArray.getJSONObject(0)
                        val userObjectOrNull = if (userObject.has("user")) {
                            userObject.getJSONObject("user")
                        } else {
                            null
                        }

                        val userDetails = if (userObject != null) {
                            parseUserDetails(userObject)
                        } else {
                            // If "user" key doesn't exist, fill UserDetails with "DOESNT EXIST" values
                            getDefaultUserDetails()
                        }

                        // Add the user details to the gifsDetails list
                        gifsDetails.add(0, userDetails.toStringArray())

                        gifUrls.add(0, gifUrl)
                        nameOfGIFs.add(0, gifName)
                        // Keep only the last 5 URLs
                        if (gifUrls.size > 5) {
                            gifUrls.removeAt(gifUrls.lastIndex)
                            nameOfGIFs.removeAt(nameOfGIFs.lastIndex)
                            gifsDetails.removeAt(gifsDetails.lastIndex)
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "That item doesn't exist in our database", Toast.LENGTH_SHORT).show()
                        }
                    }

                    displayGifs()
                    displayNames()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle other exceptions if needed
            }
        }

        thread.start()
    }


    private fun displayGifs() {
        for (i in gifUrls.indices) {
            val imageView = findViewById<ImageView>(resources.getIdentifier("maingifImageView${i + 1}", "id", packageName))
            Glide.with(this)
                .load(gifUrls[i])
                .into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
    private fun displayNames() {
        for (i in nameOfGIFs.indices) {
            val textView = findViewById<TextView>(resources.getIdentifier("textView${i + 1}", "id", packageName))
            textView.text = nameOfGIFs[i]
            textView.visibility = View.VISIBLE
        }
    }

    private fun parseUserDetails(jsonObject: JSONObject): UserDetails {
        val userObject = jsonObject.optJSONObject("user") ?: return getDefaultUserDetails()

        return UserDetails(
            avatarUrl = userObject.getString("avatar_url"),
            bannerImage = userObject.getString("banner_image"),
            bannerUrl = userObject.getString("banner_url"),
            profileUrl = userObject.getString("profile_url"),
            username = userObject.getString("username"),
            displayName = userObject.getString("display_name"),
            description = userObject.getString("description"),
            instagramUrl = userObject.getString("instagram_url"),
            websiteUrl = userObject.getString("website_url"),
            isVerified = userObject.getBoolean("is_verified")
        )
    }

    private fun getDefaultUserDetails(): UserDetails {
        return UserDetails(
            avatarUrl = "DOESNT EXIST",
            bannerImage = "DOESNT EXIST",
            bannerUrl = "DOESNT EXIST",
            profileUrl = "DOESNT EXIST",
            username = "DOESNT EXIST",
            displayName = "DOESNT EXIST",
            description = "DOESNT EXIST",
            instagramUrl = "DOESNT EXIST",
            websiteUrl = "DOESNT EXIST",
            isVerified = false
        )
    }


    private fun createStarterGif() {
        // Hide all ImageViews initially
        for (i in 1..5) {
            val imageView = findViewById<ImageView>(resources.getIdentifier("maingifImageView$i", "id", packageName))
            imageView.visibility = View.GONE
        }// Hide the default image initially
    }

    private fun hideTextView() {
        // Hide all TextViews initially
        for (i in 1..5) {
            val textView = findViewById<TextView>(resources.getIdentifier("textView$i", "id", packageName))
            textView.visibility = View.GONE
        }
    }

    // Extension function to convert UserDetails to String array
    fun UserDetails.toStringArray(): Array<String> {
        return arrayOf(
            "AVATAR URL: $avatarUrl",
            "BANNER IMAGE: $bannerImage",
            "BANNER URL: $bannerUrl",
            "PROFILE URL: $profileUrl",
            "USERNAME: $username",
            "DISPLAY NAME: $displayName",
            "DESCRIPTION: $description",
            "INSTAGRAM URL: $instagramUrl",
            "WEBSITE URL: $websiteUrl",
            "IS VERIFIED: $isVerified"
        )
    }


    fun activateGifIconForDetails() {
        val giph1: ImageView = findViewById(R.id.maingifImageView1)
        val giph2: ImageView = findViewById(R.id.maingifImageView2)
        val giph3: ImageView = findViewById(R.id.maingifImageView3)
        val giph4: ImageView = findViewById(R.id.maingifImageView4)
        val giph5: ImageView = findViewById(R.id.maingifImageView5)

        if (isImageViewNotEmpty(giph1)) {
            giph1.setOnClickListener {
                val detailsArray = gifsDetails[0]
                openGifDetailsActivity(detailsArray)
            }
        }

        if (isImageViewNotEmpty(giph2)) {
            giph2.setOnClickListener {
                val detailsArray = gifsDetails[1]
                openGifDetailsActivity(detailsArray)
            }
        }

        if (isImageViewNotEmpty(giph3)) {
            giph3.setOnClickListener{
                val detailsArray = gifsDetails[2]
                openGifDetailsActivity(detailsArray)
            }
        }

        if (isImageViewNotEmpty(giph4)) {
            giph4.setOnClickListener {
                val detailsArray = gifsDetails[3]
                openGifDetailsActivity(detailsArray)
            }
        }

        if (isImageViewNotEmpty(giph5)) {
            giph5.setOnClickListener {
                val detailsArray = gifsDetails[4]
                openGifDetailsActivity(detailsArray)
            }
        }


    }

    private fun openGifDetailsActivity(detailsArray: Array<String>) {
        val intent = Intent(this@MainActivity, GifDetailsActivity::class.java)
        intent.putExtra("detailsArray", detailsArray)
        startActivity(intent)
    }

    // Function to check if ImageView has a non-default drawable
    fun isImageViewNotEmpty(imageView: ImageView): Boolean {
        return imageView.drawable != null && imageView.drawable.constantState != null
    }


}


