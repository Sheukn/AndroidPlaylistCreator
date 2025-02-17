package com.example.androidplayistcreator.serivce

import com.example.androidplayistcreator.models.youtube.YouTubeSearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    fun searchYouTube(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        onResult: (String?) -> Unit
    ): Call<YouTubeSearchResponse>

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        fun create(): YouTubeApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(YouTubeApiService::class.java)
        }
    }
}