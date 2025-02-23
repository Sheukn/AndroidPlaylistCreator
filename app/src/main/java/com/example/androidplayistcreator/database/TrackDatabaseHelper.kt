package com.example.androidplayistcreator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TrackDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tracks.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_TRACK = "tracks"
        const val COLUMN_ARTIST = "artist"
        const val COLUMN_NAME = "name"
        const val COLUMN_STEP = "step"
        const val COLUMN_VIDEO_ID = "video_id"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_IS_SUB_TRACK = "isSubTrack"
        const val COLUMN_SOURCE = "source"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TRACK ("
                + "$COLUMN_ARTIST TEXT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_STEP INTEGER, "
                + "$COLUMN_VIDEO_ID TEXT, "
                + "$COLUMN_DURATION TEXT, "
                + "$COLUMN_IS_SUB_TRACK INTEGER, "
                + "$COLUMN_SOURCE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRACK")
        onCreate(db)
    }
}