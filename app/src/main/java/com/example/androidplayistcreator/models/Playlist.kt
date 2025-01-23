package com.example.androidplayistcreator.models

data class Playlist (
    val condition: List<Condition>,
    val creator: String,
    val id: Int,
    val name: String,
    val songs: List<Song>,
    val url: String
)