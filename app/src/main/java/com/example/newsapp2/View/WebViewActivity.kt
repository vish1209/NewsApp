package com.example.newsapp2.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.newsapp2.R
import com.facebook.shimmer.ShimmerFrameLayout

class WebViewActivity : AppCompatActivity() {
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        shimmerLayout = findViewById(R.id.shimmerLayout)
        webView = findViewById(R.id.webView)

        webView.visibility = View.GONE
        shimmerLayout.visibility = View.VISIBLE

        val url = intent.getStringExtra("url")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                shimmerLayout.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }

        if (url != null) {
            webView.loadUrl(url)
        }
    }
}