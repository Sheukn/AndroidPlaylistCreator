package com.example.androidplayistcreator.database.entities
import androidx.room.*

@Entity
data class PlaylistEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String?
)
