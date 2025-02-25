package com.example.androidplayistcreator.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidplayistcreator.database.entities.PlaylistEntity

@androidx.room.DatabaseView("SELECT * FROM PlaylistEntity INNER JOIN StepEntity ON PlaylistEntity.id = StepEntity.playlistId")
data class PlaylistWithSteps(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val steps: List<StepWithTracks>
)

