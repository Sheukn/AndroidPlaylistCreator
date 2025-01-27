package com.example.androidplayistcreator.models

data class Track(
    val artist: String,
    val name: String,
    val step: Int,
    val url: String,
    val duration: String,
    val isSubTrack: Boolean
)