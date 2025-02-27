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
        dao.insertPlaylist(PlaylistEntity(1, "My Playlist youtube", "https://example.com/image.jpg"))
        dao.insertPlaylist(PlaylistEntity(2, "My Playlist audius", "https://example.com/image.jpg"))
        val steps = listOf(
            StepEntity(step = 1, playlistId = 1),
            StepEntity(step = 2, playlistId = 1),
            StepEntity(step = 3, playlistId = 1),
            StepEntity(step = 1, playlistId = 2),
            StepEntity(step = 2, playlistId = 2),
            StepEntity(step = 3, playlistId = 2),
        )
        val stepIds = dao.insertSteps(steps)

        val tracks = listOf(
            TrackEntity(artist = "Artist 1", name = "Never", stepId = stepIds[0].toInt(), videoId = "youtube.com/vid1", duration = "3:30", isSubTrack = false, source = "youtube"),
//            TrackEntity(artist = "Artist 2", name = "Gonna", stepId = stepIds[1].toInt(), videoId = "youtube.com/vid2", duration = "4:00", isSubTrack = false, source = "youtube"),
            TrackEntity(artist = "Artist 3", name = "Give", stepId = stepIds[1].toInt(), videoId = "youtube.com/vid3", duration = "2:45", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 4", name = "You", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid4", duration = "3:15", isSubTrack = false, source = "youtube"),
            TrackEntity(artist = "Artist 5", name = "Up", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid5", duration = "2:30", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 6", name = "Let", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid6", duration = "2:50", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "Artist 7", name = "You", stepId = stepIds[2].toInt(), videoId = "youtube.com/vid7", duration = "3:10", isSubTrack = true, source = "youtube"),
            TrackEntity(artist = "JumpmannTR", name = "never gonna give you up remix", stepId = stepIds[3].toInt(), videoId = "b4zw0kP", duration = "3:30", isSubTrack = false, source = Source.AUDIUS.toString()),
            TrackEntity(artist = "Ugliifroot", name = "A-HA - TAKE ON ME [TH!S COVER]", stepId = stepIds[3].toInt(), videoId = "lZ4jK", duration = "2:45", isSubTrack = true, source = Source.AUDIUS.toString()),
            TrackEntity(artist = "Djtaddaa", name = "Baby Shark", stepId = stepIds[4].toInt(), videoId = "W06bv", duration = "2:45", isSubTrack = false, source = Source.AUDIUS.toString()),
            TrackEntity(artist = "B-rizzle", name = "Under Pressure", stepId = stepIds[5].toInt(), videoId = "P5joQNp", duration = "2:45", isSubTrack = false, source = Source.AUDIUS.toString()),

            )
        dao.insertTracks(tracks)
    }

    suspend fun clearDatabase() {
        dao.deleteAllTracks()
        dao.deleteAllSteps()
        dao.deleteAllPlaylists()
    }
}
