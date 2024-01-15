package org.unizd.rma.roncevic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.unizd.rma.roncevic.R

class RestSoldierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rest_soldier_activity)

        val gifImageView: ImageView = findViewById(R.id.gifImageView)

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load("https://i.kym-cdn.com/photos/images/newsfeed/002/336/753/653.gif")
            .into(gifImageView)

        val buttonHome: Button = findViewById(R.id.button1)
        val buttonCampfire: Button = findViewById(R.id.button2)

        buttonHome.setOnClickListener {
            // Open Main Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonCampfire.setOnClickListener {
            // Open Rest Soldier Activity
            val intent = Intent(this, RestSoldierActivity::class.java)
            startActivity(intent)
        }
    }
}