package com.example.androidplayistcreator.models.singletons

import com.example.androidplayistcreator.models.Playlist

object PlaylistSingleton {
    var playlist: Playlist = Playlist()

    fun clear() {
        playlist = Playlist()
    }
}
