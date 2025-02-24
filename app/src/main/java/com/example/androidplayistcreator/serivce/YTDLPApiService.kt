package com.example.androidplayistcreator.serivce

import com.example.androidplayistcreator.models.player.AudioResponse
import com.example.androidplayistcreator.models.player.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface YTDLPApiService {

    @POST("get_audio")
    fun getAudioUrl(@Body request: Map<String, String>): Call<AudioResponse>

    @GET("search")
    fun searchYoutubeVideos(@Query("query") query: String): Call<SearchResponse>

    companion object {
        private const val BASE_URL = "http://192.168.1.201:5000/"  // Replace with your backend URL

        fun create(): YTDLPApiService {
            // Create the logging interceptor
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // Create OkHttp client with the interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Build the Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(YTDLPApiService::class.java)
        }
    }
}