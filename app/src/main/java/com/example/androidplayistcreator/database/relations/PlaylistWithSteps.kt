package com.example.androidplayistcreator.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidplayistcreator.database.entities.PlaylistEntity

data class PlaylistWithSteps(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val steps: List<StepWithTracks>
)

