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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createStarterGif()
        hideTextView()

        val buttonHome: Button = findViewById(R.id.button1)
        val buttonCampfire: Button = findViewById(R.id.button2)
        val searchBar: EditText = findViewById(R.id.search_bar)

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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
        val apiKey = "YOUR API KEY"
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

                        gifUrls.add(0, gifUrl)
                        nameOfGIFs.add(0, gifName)
                        // Keep only the last 5 URLs
                        if (gifUrls.size > 5) {
                            gifUrls.removeAt(gifUrls.lastIndex)
                            nameOfGIFs.removeAt(nameOfGIFs.lastIndex)
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
            textView.setText(nameOfGIFs[i])
            textView.visibility = View.VISIBLE
        }
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


}


