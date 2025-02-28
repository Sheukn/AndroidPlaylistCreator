package com.example.androidplayistcreator.database

import android.content.Context
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.database.entities.StepEntity
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.models.Source


class DatabaseMigrator(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.playlistDao()

    suspend fun migrate() {
        dao.insertPlaylist(PlaylistEntity(2, "My Playlist audius THE VERY LONG TEXT FOR TESTS", "https://example.com/image.jpg"))
        val steps = listOf(
            StepEntity(step = 1, playlistId = 2),
            StepEntity(step = 2, playlistId = 2),
            StepEntity(step = 3, playlistId = 2),
        )
        val stepIds = dao.insertSteps(steps)

        val tracks = listOf(
            TrackEntity(id = 1, artist = "JumpmannTR", name = "never gonna give you up remix JEN AI MARRE", stepId = stepIds[0].toInt(), video_id = "b4zw0kP", duration = "3:30", isSubTrack = false, source = Source.AUDIUS.toString()),
            TrackEntity(id = 2, artist = "Ugliifroot", name = "A-HA - TAKE ON ME [TH!S COVER]", stepId = stepIds[0].toInt(), video_id = "lZ4jK", duration = "2:45", isSubTrack = true, source = Source.AUDIUS.toString()),
            TrackEntity(id = 3, artist = "Djtaddaa", name = "Baby Shark", stepId = stepIds[1].toInt(), video_id = "W06bv", duration = "2:45", isSubTrack = false, source = Source.AUDIUS.toString()),
            TrackEntity(id = 4, artist = "B-rizzle", name = "Under Pressure", stepId = stepIds[2].toInt(), video_id = "P5joQNp", duration = "2:45", isSubTrack = false, source = Source.AUDIUS.toString()),

            )
        dao.insertTracks(tracks)
    }

    suspend fun clearDatabase() {
        dao.deleteAllTracks()
        dao.deleteAllSteps()
        dao.deleteAllPlaylists()
    }
}