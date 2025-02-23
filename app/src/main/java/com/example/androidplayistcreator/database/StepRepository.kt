package com.example.androidplayistcreator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track

class StepRepository(context: Context) {
    private val dbHelper = StepDatabaseHelper(context)

    fun addStep(step: Step): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(StepDatabaseHelper.COLUMN_STEP, step.step)
            put(StepDatabaseHelper.COLUMN_MAIN_TRACK, step.mainTrack.toString()) // Assuming Track has a proper toString() method
            put(StepDatabaseHelper.COLUMN_SUB_TRACKS, step.subTracks.joinToString { it.toString() }) // Assuming Track has a proper toString() method
        }
        return db.insert(StepDatabaseHelper.TABLE_STEP, null, values)
    }

    fun getAllSteps(): List<Step> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            StepDatabaseHelper.TABLE_STEP,
            null, null, null, null, null, null
        )
        val steps = mutableListOf<Step>()
        with(cursor) {
            while (moveToNext()) {
                val stepNumber = getInt(getColumnIndexOrThrow(StepDatabaseHelper.COLUMN_STEP))
                val mainTrack = Track.fromString(getString(getColumnIndexOrThrow(StepDatabaseHelper.COLUMN_MAIN_TRACK))) // Assuming Track has a fromString() method
                val subTracks = getString(getColumnIndexOrThrow(StepDatabaseHelper.COLUMN_SUB_TRACKS)).split(",").map { Track.fromString(it) } // Assuming Track has a fromString() method
                steps.add(Step(stepNumber, mainTrack, subTracks))
            }
        }
        cursor.close()
        return steps
    }

    fun updateStep(step: Step): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(StepDatabaseHelper.COLUMN_STEP, step.step)
            put(StepDatabaseHelper.COLUMN_MAIN_TRACK, step.mainTrack.toString()) // Assuming Track has a proper toString() method
            put(StepDatabaseHelper.COLUMN_SUB_TRACKS, step.subTracks.joinToString { it.toString() }) // Assuming Track has a proper toString() method
        }
        return db.update(StepDatabaseHelper.TABLE_STEP, values, "${StepDatabaseHelper.COLUMN_STEP}=?", arrayOf(step.step.toString()))
    }

    fun deleteStep(stepNumber: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(StepDatabaseHelper.TABLE_STEP, "${StepDatabaseHelper.COLUMN_STEP}=?", arrayOf(stepNumber.toString()))
    }
}