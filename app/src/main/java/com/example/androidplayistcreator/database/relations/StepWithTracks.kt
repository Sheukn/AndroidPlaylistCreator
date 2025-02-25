package com.example.androidplayistcreator.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidplayistcreator.database.entities.StepEntity
import com.example.androidplayistcreator.database.entities.TrackEntity

data class StepWithTracks(
    @Embedded val step: StepEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "stepId"
    )
    val tracks: List<TrackEntity>
)
