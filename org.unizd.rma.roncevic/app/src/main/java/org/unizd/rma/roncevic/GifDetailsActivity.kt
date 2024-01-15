package org.unizd.rma.roncevic

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.unizd.rma.roncevic.R

class GifDetailsActivity : AppCompatActivity() {
    private lateinit var detailsArray: Array<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the detailsArray in your activity as needed
        detailsArray = intent.getStringArrayExtra("detailsArray") ?: emptyArray()
        setContentView(R.layout.gif_details)
        initializeTextViews()

    }

    // Initializing values from list of details.
    private fun initializeTextViews() {
        val textViewDetails1: TextView = findViewById(R.id.textViewDeatails1)
        val textViewDetails2: TextView = findViewById(R.id.textViewDeatails2)
        val textViewDetails3: TextView = findViewById(R.id.textViewDeatails3)
        val textViewDetails4: TextView = findViewById(R.id.textViewDeatails4)
        val textViewDetails5: TextView = findViewById(R.id.textViewDeatails5)
        val textViewDetails6: TextView = findViewById(R.id.textViewDeatails6)
        val textViewDetails7: TextView = findViewById(R.id.textViewDeatails7)
        val textViewDetails8: TextView = findViewById(R.id.textViewDeatails8)
        val textViewDetails9: TextView = findViewById(R.id.textViewDeatails9)
        val textViewDetails10: TextView = findViewById(R.id.textViewDeatails10)

        // Set the text for each TextView based on UserDetails
        textViewDetails1.text = detailsArray[0]
        textViewDetails2.text = detailsArray[1]
        textViewDetails3.text = detailsArray[2]
        textViewDetails4.text = detailsArray[3]
        textViewDetails5.text = detailsArray[4]
        textViewDetails6.text = detailsArray[5]
        textViewDetails7.text = detailsArray[6]
        textViewDetails8.text = detailsArray[7]
        textViewDetails9.text = detailsArray[8]
        textViewDetails10.text = detailsArray[9]
    }
}
