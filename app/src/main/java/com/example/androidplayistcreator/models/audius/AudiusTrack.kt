package com.example.androidplayistcreator.models.audius

data class AudiusTrack(
    val title: String,
    val id: String,
    val artist: AudiusUser?,
    val duration: String,
    val isStreamble: Boolean,
    val artwork: AudiusArtwork?
)