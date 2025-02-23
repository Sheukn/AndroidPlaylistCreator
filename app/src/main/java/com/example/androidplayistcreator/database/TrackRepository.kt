package com.example.androidplayistcreator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.androidplayistcreator.models.Track

class TrackRepository(context: Context) {
    private val dbHelper = TrackDatabaseHelper(context)

    fun addTrack(track: Track): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TrackDatabaseHelper.COLUMN_ARTIST, track.artist)
            put(TrackDatabaseHelper.COLUMN_NAME, track.name)
            put(TrackDatabaseHelper.COLUMN_STEP, track.step)
            put(TrackDatabaseHelper.COLUMN_VIDEO_ID, track.video_id)
            put(TrackDatabaseHelper.COLUMN_DURATION, track.duration)
            put(TrackDatabaseHelper.COLUMN_IS_SUB_TRACK, if (track.isSubTrack) 1 else 0)
            put(TrackDatabaseHelper.COLUMN_SOURCE, track.source)
        }
        return db.insert(TrackDatabaseHelper.TABLE_TRACK, null, values)
    }

    fun getTracksByPlaylistId(playlistId: Int): List<Track> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            TrackDatabaseHelper.TABLE_TRACK,
            null, "${TrackDatabaseHelper.COLUMN_STEP}=?", arrayOf(playlistId.toString()), null, null, null
        )
        val tracks = mutableListOf<Track>()
        with(cursor) {
            while (moveToNext()) {
                val artist = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_ARTIST))
                val name = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_NAME))
                val step = getInt(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_STEP))
                val video_id = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_VIDEO_ID))
                val duration = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_DURATION))
                val isSubTrack = getInt(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_IS_SUB_TRACK)) == 1
                val source = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_SOURCE))
                tracks.add(Track(artist, name, step, video_id, duration, isSubTrack, source))
            }
        }
        cursor.close()
        return tracks
    }

    fun getAllTracks(): List<Track> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            TrackDatabaseHelper.TABLE_TRACK,
            null, null, null, null, null, null
        )
        val tracks = mutableListOf<Track>()
        with(cursor) {
            while (moveToNext()) {
                val artist = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_ARTIST))
                val name = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_NAME))
                val step = getInt(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_STEP))
                val video_id = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_VIDEO_ID))
                val duration = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_DURATION))
                val isSubTrack = getInt(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_IS_SUB_TRACK)) == 1
                val source = getString(getColumnIndexOrThrow(TrackDatabaseHelper.COLUMN_SOURCE))
                tracks.add(Track(artist, name, step, video_id, duration, isSubTrack, source))
            }
        }
        cursor.close()
        return tracks
    }

    fun updateTrack(track: Track): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TrackDatabaseHelper.COLUMN_ARTIST, track.artist)
            put(TrackDatabaseHelper.COLUMN_NAME, track.name)
            put(TrackDatabaseHelper.COLUMN_STEP, track.step)
            put(TrackDatabaseHelper.COLUMN_VIDEO_ID, track.video_id)
            put(TrackDatabaseHelper.COLUMN_DURATION, track.duration)
            put(TrackDatabaseHelper.COLUMN_IS_SUB_TRACK, if (track.isSubTrack) 1 else 0)
            put(TrackDatabaseHelper.COLUMN_SOURCE, track.source)
        }
        return db.update(TrackDatabaseHelper.TABLE_TRACK, values, "${TrackDatabaseHelper.COLUMN_VIDEO_ID}=?", arrayOf(track.video_id))
    }

    fun deleteTrack(video_id: String): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TrackDatabaseHelper.TABLE_TRACK, "${TrackDatabaseHelper.COLUMN_VIDEO_ID}=?", arrayOf(video_id))
    }
}