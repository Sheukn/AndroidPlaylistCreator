package com.example.androidplayistcreator.models

data class Playlist(
    val id: Int = 0,
    val name: String = "",
    val steps: List<Step> = emptyList(),
    val imageUrl: String? = null
)