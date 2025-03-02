package com.example.androidplayistcreator.models

data class Playlist(
    val id: Int = 0,
    val name: String = "",
    val steps: MutableList<Step> = mutableListOf(),
    val imageUrl: String? = null
)