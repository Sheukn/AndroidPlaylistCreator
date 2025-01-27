package com.example.androidplayistcreator.models

data class Step(
    val step: Int,
    val mainTrack: Track,
    val subTracks: List<Track>
)