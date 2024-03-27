package com.example.newsapp.HomePageNews

import com.example.newsapp2.Model.HeadLineData
import com.example.newsapp2.Model.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface headlineapi {
//3ad8a16aae514436b2178d0ce81d75d7
    //bdc75872219a4a3b9e0430d833ca356d
    @GET("v2/top-headlines?country=in&apiKey=bdc75872219a4a3b9e0430d833ca356d")
    fun getHeadlinesHorizontalScroll() : Call<HeadLineData>


    @GET("v2/everything?q=keyword&apiKey=3ad8a16aae514436b2178d0ce81d75d7")
    fun getHeadlines() : Call<HeadLineData>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey:String ="3ad8a16aae514436b2178d0ce81d75d7"
    ):Response<NewsResponse>
}