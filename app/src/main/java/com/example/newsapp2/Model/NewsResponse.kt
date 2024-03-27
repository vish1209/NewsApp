package com.example.newsapp2.Model

import com.example.newsapp2.Model.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
