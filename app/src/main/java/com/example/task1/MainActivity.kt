package com.example.task1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat

class MainActivity : AppCompatActivity() {

    private lateinit var chosenText:TextView
    private lateinit var receivedText:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chosenText = findViewById(R.id.chosenText)
        receivedText = findViewById(R.id.receivedText)

        val intent = intent
        val message = intent.extras?.get(Intent.EXTRA_TEXT)

        if (message != null) {
            receivedText.text = message.toString()
            receivedText.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            chosenText.text = data.getStringExtra("result")
        }
    }


    fun startChooseActivity(view: View) {
        val intent = Intent(this, ChooseActivity::class.java)
        startActivityForResult(intent, 1)
    }


    fun shareText(view: View) {
        val string = chosenText.text.toString()
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(this)
            .setType(mimeType)
            .setChooserTitle("Share this text with: ")
            .setText(string)
            .startChooser()
}
    }