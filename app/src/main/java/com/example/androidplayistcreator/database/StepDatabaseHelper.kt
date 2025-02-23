package com.example.androidplayistcreator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StepDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "steps.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_STEP = "steps"
        const val COLUMN_STEP = "step"
        const val COLUMN_MAIN_TRACK = "mainTrack"
        const val COLUMN_SUB_TRACKS = "subTracks"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_STEP ("
                + "$COLUMN_STEP INTEGER PRIMARY KEY, "
                + "$COLUMN_MAIN_TRACK TEXT, "
                + "$COLUMN_SUB_TRACKS TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STEP")
        onCreate(db)
    }
}