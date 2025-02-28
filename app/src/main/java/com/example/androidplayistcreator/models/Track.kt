package com.example.androidplayistcreator.models

import com.example.androidplayistcreator.database.entities.TrackEntity

data class Track(
    val id: Int? = null,
    val artist: String? = null,
    val name: String,
    val audiusId: String,
    val duration: String,
    val isStreamble: Boolean,
    val step: Int,
    val isSubTrack: Boolean,
    val source: String?,
    val artwork: String?
) {
    companion object {
        fun fromString(trackString: String): Track {
            val parts = trackString.split("|")
            return Track(
                id = -1,
                artist = parts[0],
                name = parts[1],
                step = parts[2].toInt(),
                audiusId = parts[3],
                duration = parts[4],
                isSubTrack = parts[5].toBoolean(),
                source = if (parts.size > 6) parts[6] else null,
                isStreamble = parts[7].toBoolean(),
                artwork = if (parts.size > 8) parts[8] else null
            )
        }

        fun fromEntity(trackEntity: TrackEntity): Track {
            return Track(
                id = trackEntity.id,
                artist = trackEntity.artist,
                name = trackEntity.name,
                audiusId = trackEntity.audiusId,
                duration = trackEntity.duration,
                isStreamble = trackEntity.isStreamable,
                step = trackEntity.stepId,
                isSubTrack = trackEntity.isSubTrack,
                source = trackEntity.source,
                artwork = trackEntity.artwork
            )
        }
    }

    override fun toString(): String {
        return listOf(artist, name, step, audiusId, duration, isSubTrack , source, isStreamble, artwork)
            .joinToString("|") { it.toString() }
    }
}
