package com.example.androidplayistcreator.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val step: Int,
    val playlistId: Int,
)
