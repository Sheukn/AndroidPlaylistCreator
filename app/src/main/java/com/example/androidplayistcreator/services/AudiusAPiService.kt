package com.example.androidplayistcreator.services

import com.example.androidplayistcreator.apis.AudiusApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AudiusService {
    private const val BASE_URL = "https://discoveryprovider.audius.co/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AudiusApi = retrofit.create(AudiusApi::class.java)

    fun getStreamUrl(trackId: String): String {
        return "https://discoveryprovider.audius.co/v1/tracks/$trackId/stream"
    }
}
