package com.example.androidplayistcreator.models

import android.os.Parcel
import android.os.Parcelable
import com.example.androidplayistcreator.database.entities.TrackEntity

data class Track(
    val id: Int? = null,
    val artist: String? = null,
    val name: String,
    val audiusId: String,
    val duration: String,
    val isStreamable: Boolean,
    val step: Int,
    val isSubTrack: Boolean,
    val source: String?,
    val artwork: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(artist)
        parcel.writeString(name)
        parcel.writeString(audiusId)
        parcel.writeString(duration)
        parcel.writeByte(if (isStreamable) 1 else 0)
        parcel.writeInt(step)
        parcel.writeByte(if (isSubTrack) 1 else 0)
        parcel.writeString(source)
        parcel.writeString(artwork)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }

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
                isStreamable = parts[7].toBoolean(),
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
                isStreamable = trackEntity.isStreamable,
                step = trackEntity.stepId,
                isSubTrack = trackEntity.isSubTrack,
                source = trackEntity.source,
                artwork = trackEntity.artwork
            )
        }
    }

    override fun toString(): String {
        return listOf(artist, name, step, audiusId, duration, isSubTrack, source, isStreamable, artwork)
            .joinToString("|") { it.toString() }
    }
}
