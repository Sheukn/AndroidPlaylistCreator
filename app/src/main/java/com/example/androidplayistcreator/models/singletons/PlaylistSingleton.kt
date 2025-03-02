package com.example.androidplayistcreator.models.singletons

import com.example.androidplayistcreator.models.Playlist

object PlaylistSingleton {
    var playlist: Playlist = Playlist()
    var currentStepPosition: Int = -1

    fun clear() {
        playlist = Playlist()
        currentStepPosition = -1
    }
}
