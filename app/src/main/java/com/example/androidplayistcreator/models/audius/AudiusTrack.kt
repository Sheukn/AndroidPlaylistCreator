package com.example.androidplayistcreator.models.audius

data class AudiusTrack(
    val id: String,
    val title: String,
    val duration: Int,
    val permalink: String,
    val user: AudiusUser
)
