package com.example.androidplayistcreator.database.entities
import androidx.room.*

@Entity
data class PlaylistEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUrl: String?
)
