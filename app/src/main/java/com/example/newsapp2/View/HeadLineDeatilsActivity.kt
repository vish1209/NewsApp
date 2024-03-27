package com.example.newsapp2.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import com.bumptech.glide.Glide
import com.example.newsapp2.R

class HeadLineDeatilsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_head_line_deatils)

        var detail_headline_view_image = findViewById<ImageView>(R.id.detail_headline_view_image)
        var detail_headline_view_headline =
            findViewById<TextView>(R.id.detail_headline_view_headline)
        var detail_headline_view_description =
            findViewById<TextView>(R.id.detail_headline_view_description)
        val tv_read_more = findViewById<TextView>(R.id.tv_read_more)

        val article_Image = intent.getStringExtra("urlToImage")
        val article_headline = intent.getStringExtra("title")
        val article_description = intent.getStringExtra("content")
        val articleUrl = intent.getStringExtra("url")


        Glide.with(this)
            .load(article_Image)
            .into(detail_headline_view_image)


        detail_headline_view_headline.text = article_headline ?: "No Headline Available"
        detail_headline_view_description.text = article_description?.substringBefore(" [+") ?: "No Description Available"

        tv_read_more.setOnClickListener {
            articleUrl?.let { url ->
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
        }

    }
}