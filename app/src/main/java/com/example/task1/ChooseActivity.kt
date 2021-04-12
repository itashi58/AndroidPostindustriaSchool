package com.example.task1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.log

class ChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        // TODO: 4/12/21 code style formatting
        val button1:Button = findViewById(R.id.button1)
        val button2:Button = findViewById(R.id.button2)
        val button3:Button = findViewById(R.id.button3)
        val button4:Button = findViewById(R.id.button4)
        val button5:Button = findViewById(R.id.button5)

        button1.setOnClickListener {getResultToMainActivity(button1)}
        button2.setOnClickListener {getResultToMainActivity(button2)}
        button3.setOnClickListener {getResultToMainActivity(button3)}
        button4.setOnClickListener {getResultToMainActivity(button4)}
        button5.setOnClickListener {getResultToMainActivity(button5)}

    }

    //taking text from the button and sending it as a result to main activity
    // TODO: 4/12/21 `get` implies that you return something from a method. More proper name would be `sendResult...`
    private fun getResultToMainActivity(button: Button) {
        val intent = Intent(this , MainActivity::class.java)
        intent.putExtra("result", button.text)
        setResult(RESULT_OK, intent)
        finish()
    }
}