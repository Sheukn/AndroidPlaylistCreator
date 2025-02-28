package com.example.androidplayistcreator.apis

import com.example.androidplayistcreator.models.audius.AudiusData
import com.example.androidplayistcreator.models.audius.AudiusResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AudiusApi {
    @GET("tracks/search")
    suspend fun searchTracks(
        @Query("query") query: String,
        @Query("limit") limit: Int = 10
    ): AudiusResponse

    @GET("tracks/{track_id}")
    suspend fun getTrack(@Path("track_id") trackId: String): AudiusData
}
