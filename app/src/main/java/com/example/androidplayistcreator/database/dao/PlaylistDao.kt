package com.example.androidplayistcreator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.database.entities.StepEntity
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.database.relations.PlaylistWithSteps

@Dao
interface PlaylistDao {
    @Transaction
    @Query("SELECT * FROM PlaylistEntity WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistWithSteps


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert
    suspend fun insertSteps(steps: List<StepEntity>): List<Long>

    @Insert
    suspend fun insertTracks(tracks: List<TrackEntity>): List<Long>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM TrackEntity")
    suspend fun deleteAllTracks()

    @Query("DELETE FROM StepEntity")
    suspend fun deleteAllSteps()

    @Query("DELETE FROM PlaylistEntity")
    suspend fun deleteAllPlaylists()

    @Query("SELECT * FROM PlaylistEntity WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("SELECT * FROM StepEntity WHERE playlistId = :playlistId")
    suspend fun getStepsByPlaylistId(playlistId: Int): List<StepEntity>

    @Query("SELECT * FROM TrackEntity WHERE stepId = :stepId")
    suspend fun getTracksByStepId(stepId: Int): List<TrackEntity>
}