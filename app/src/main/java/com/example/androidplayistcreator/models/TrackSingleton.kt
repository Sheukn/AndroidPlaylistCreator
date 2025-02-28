package com.example.androidplayistcreator.models

import com.example.androidplayistcreator.database.entities.TrackEntity

object TrackSingleton {
    private var currentTrack: TrackEntity? = null

    // Get the current track
    fun getCurrentTrackId(): Int {
        return currentTrack?.id ?: 0
    }

    // Set the current track
    fun setCurrentTrack(track: TrackEntity) {
        currentTrack = track
    }

    // Clear the current track
    fun clearTrack() {
        currentTrack = null
    }
}
