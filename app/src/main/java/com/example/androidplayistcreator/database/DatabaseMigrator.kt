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
            TrackEntity(
                id = 1,
                artist = "JumpmannTR",
                name = "never gonna give you up remix JEN AI MARRE",
                stepId = stepIds[0].toInt(),
                audiusId = "b4zw0kP",
                duration = "3:30",
                isSubTrack = false,
                source = Source.AUDIUS.toString(),
                isStreamable = true,
                artwork = "https://audius-content-2.figment.io/content/01H4Y0SPQEMZV6M497Z1JWZ30S/1000x1000.jpg"
            ),
            TrackEntity(
                id = 2,
                artist = "Ugliifroot",
                name = "A-HA - TAKE ON ME [TH!S COVER]",
                stepId = stepIds[0].toInt(),
                audiusId = "lZ4jK",
                duration = "2:45",
                isSubTrack = true,
                source = Source.AUDIUS.toString(),
                isStreamable = true,
                artwork = "https://audius-creator-9.theblueprint.xyz/content/QmVcm3f5U3p5SqnECBLnhMZDMYqnZKKLouLdvZkXQCU9Q9/1000x1000.jpg"
            ),
            TrackEntity(
                id = 3,
                artist = "Djtaddaa",
                name = "Baby Shark",
                stepId = stepIds[1].toInt(),
                audiusId = "W06bv",
                duration = "2:45",
                isSubTrack = false,
                source = Source.AUDIUS.toString(),
                isStreamable = true,
                artwork = "https://audius-content-1.figment.io/content/QmUSweFpzoipm22dpiNB6Z2yto7pm4Uh65G3TPcud6ukcr/1000x1000.jpg"
            ),
            TrackEntity(
                id = 4,
                artist = "B-rizzle",
                name = "Under Pressure",
                stepId = stepIds[2].toInt(),
                audiusId = "P5joQNp",
                duration = "2:45",
                isSubTrack = false,
                source = Source.AUDIUS.toString(),
                isStreamable = true,
                artwork = "https://blockdaemon-audius-content-04.bdnodes.net/content/01HNNRG8W60QV9FHSPPB199VVN/1000x1000.jpg"
            ),

            )
        dao.insertTracks(tracks)
    }

    suspend fun clearDatabase() {
        dao.deleteAllTracks()
        dao.deleteAllSteps()
        dao.deleteAllPlaylists()
    }
}