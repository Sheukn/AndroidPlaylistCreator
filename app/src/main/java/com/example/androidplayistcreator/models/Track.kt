package com.example.androidplayistcreator.models

import com.example.androidplayistcreator.database.entities.TrackEntity

data class Track(
    val id: Int? = null,
    val artist: String,
    val name: String,
    val step: Int,
    val video_id: String,
    val duration: String,
    val isSubTrack: Boolean,
    val source: String? = null
) {
    companion object {
        fun fromString(trackString: String): Track {
            val parts = trackString.split("|")
            return Track(
                artist = parts[0],
                name = parts[1],
                step = parts[2].toInt(),
                video_id = parts[3],
                duration = parts[4],
                isSubTrack = parts[5].toBoolean(),
                source = if (parts.size > 6) parts[6] else null
            )
        }

        fun fromEntity(trackEntity: TrackEntity): Track {
            return Track(
                artist = trackEntity.artist,
                name = trackEntity.name,
                step = trackEntity.stepId,
                video_id = trackEntity.video_id,
                duration = trackEntity.duration,
                isSubTrack = trackEntity.isSubTrack,
                source = trackEntity.source
            )
        }
    }

    override fun toString(): String {
        return listOf(artist, name, step, video_id, duration, isSubTrack, source).joinToString("|")
    }
}