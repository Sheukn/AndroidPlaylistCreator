package com.example.androidplayistcreator.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artist: String,
    val name: String,
    val stepId: Int,
    val videoId: String,
    val duration: String,
    val isSubTrack: Boolean,
    val source: String?
)

