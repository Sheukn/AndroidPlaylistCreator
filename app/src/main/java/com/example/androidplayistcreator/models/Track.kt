package com.example.androidplayistcreator.models

import kotlin.time.Duration

data class Track(
    val artist: String,
    val name: String,
    val step: Int,
    val url: String,
    val duration: String
)