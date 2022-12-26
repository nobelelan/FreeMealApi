package com.example.freemealapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.freemealapi.R
import com.example.freemealapi.databinding.ActivityYoutubeWebBinding

class YoutubeWebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYoutubeWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ytUrl = intent.getStringExtra("ytUrl")

        binding.youtubeWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            if (ytUrl.isNullOrEmpty()){
                Toast.makeText(this@YoutubeWebActivity, "Error loading video!", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                loadUrl(ytUrl)
            }
        }
    }
}