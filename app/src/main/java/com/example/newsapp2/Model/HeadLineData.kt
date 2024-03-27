package com.example.newsapp2.Model

import com.example.newsapp2.Model.Article

data class HeadLineData(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)