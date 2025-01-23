package com.example.androidplayistcreator.models

data class User(
    val email: String,
    val name: String,
    val playistCreated: List<Playlist>,
    val playistSubbed: List<Playlist>
)