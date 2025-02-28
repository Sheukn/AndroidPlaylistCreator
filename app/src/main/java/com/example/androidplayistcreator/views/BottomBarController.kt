package com.example.androidplayistcreator.views

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomBarController(
    private val bottomBar: ConstraintLayout,
    private val trackId : Int,
    private val context: Context
) {
    private val audioTitleTextView: TextView = bottomBar.findViewById(R.id.audioTitleTextView)
    private val audioArtistTextView: TextView = bottomBar.findViewById(R.id.audioArtistTextView)
    private val audioImageView: ImageView = bottomBar.findViewById(R.id.audioImageView)
    private val playlistDao: PlaylistDao = AppDatabase.getInstance(context).playlistDao()

    init {
        loadTrack()
    }

    private fun loadTrack() {
        CoroutineScope(Dispatchers.IO).launch {
            val track = playlistDao.getTrackById(trackId)
            Log.d("BottomBarController", "Loaded track: $track")
            withContext(Dispatchers.Main) {
                track.let {
                    audioTitleTextView.text = it.name
                    audioArtistTextView.text = it.artist

                }
            }
        }
    }

    fun show() {
        bottomBar.visibility = ConstraintLayout.VISIBLE
    }

    fun hide() {
        bottomBar.visibility = ConstraintLayout.GONE
    }

}