package com.example.androidplayistcreator.models

import com.example.androidplayistcreator.database.relations.StepWithTracks

data class Step(
    val step: Int = 0,
    var mainTrack: Track,
    val subTracks: MutableList<Track> = mutableListOf()
) {
    companion object {
        fun fromString(stepString: String): Step {
            val parts = stepString.split("|")
            return Step(
                step = parts[0].toInt(),
                mainTrack = Track.fromString(parts[1]),
                subTracks = parts.subList(2, parts.size).map { Track.fromString(it) }.toMutableList()
            )
        }

        fun fromStepsWithTracksEntity(stepWithTracks: StepWithTracks): Step {
            return Step(
                step = stepWithTracks.step.id,
                mainTrack = Track.fromEntity(stepWithTracks.tracks[0]),
                subTracks = stepWithTracks.tracks.subList(1, stepWithTracks.tracks.size).map { Track.fromEntity(it) }.toMutableList()
            )
        }
    }
}