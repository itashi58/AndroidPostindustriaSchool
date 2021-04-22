package com.example.androidpostindustriaschool.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.androidpostindustriaschool.R

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webView)
        // TODO: 4/22/21 redundant field
        val recivedIntent = intent
        if (intent.data != null) {
            webView.loadUrl(intent.data.toString())
        }

    }
}