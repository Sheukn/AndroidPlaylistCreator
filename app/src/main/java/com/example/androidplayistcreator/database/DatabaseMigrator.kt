package com.example.androidplayistcreator.database

import android.content.Context
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.database.entities.StepEntity
import com.example.androidplayistcreator.database.entities.TrackEntity


class DatabaseMigrator(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.playlistDao()

    suspend fun migrate() {
        dao.insertPlaylist(PlaylistEntity(1, "My Playlist", "https://example.com/image.jpg"))

        val steps = listOf(
            StepEntity(step = 1, playlistId = 1),
            StepEntity(step = 2, playlistId = 1),
            StepEntity(step = 3, playlistId = 1)
        )
        val stepIds = dao.insertSteps(steps)

        val tracks = listOf(
            TrackEntity(artist = "Artist 1", name = "Main Track 1", stepId = stepIds[0].toInt(), videoId = "youtube.com/vid1", duration = "3:30", isSubTrack = false, source = "youtube"),
            TrackEntity(artist = "Artist 2", name = "Main Track 2", stepId = stepIds[1].toInt(), videoId = "youtube.com/vid2", duration = "4:00", isSubTrack = false, source = "youtube"),
            TrackEntity(artist = "Artist 3", name = "Sub Track 1", stepId = stepIds[1].toInt(), videoId = "youtube.com/vid3", duration = "2:45", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 4", name = "Main Track 3", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid4", duration = "3:15", isSubTrack = false, source = "youtube"),
            TrackEntity(artist = "Artist 5", name = "Sub Track 1", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid5", duration = "2:30", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 6", name = "Sub Track 2", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid6", duration = "2:50", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 7", name = "Sub Track 3", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid7", duration = "3:10", isSubTrack = true, source = "youtube")
        )
        dao.insertTracks(tracks)
    }
}
