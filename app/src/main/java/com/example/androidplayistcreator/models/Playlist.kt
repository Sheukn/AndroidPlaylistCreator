package com.example.androidplayistcreator.models

data class Playlist (
    val condition: List<Condition>,
    val id: Int,
    val name: String,
    val steps: List<Step>,
    val imageUrl: String?
)