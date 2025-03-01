package com.example.androidplayistcreator.services

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.TrackSingleton
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class MusicService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private var binder = MusicBinder()
    private lateinit var seekBar: SeekBar
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun setTrack(track: Track) {
        // Load the track
        var url = "https://discoveryprovider.audius.co/v1/tracks/${track.audiusId}/stream"
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun playTrack(track: Track) {
        val trackEntity = TrackEntity(
            id = track.id ?: 0, // Ensure id is not null
            name = track.name,
            artist = track.artist ?: "Unknown",
            audiusId = track.audiusId,
            source = track.source ?: "AUDIUS",
            duration = track.duration,
            isSubTrack = track.isSubTrack,
            stepId = track.step,
            isStreamable = track.isStreamable,
            artwork = track.artwork
        )
        TrackSingleton.setCurrentTrack(trackEntity)
        if (!exoPlayer.isPlaying) {
            exoPlayer.play()
        }
    }

    fun pauseTrack() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    fun seekTo(position: Int) {
        exoPlayer.seekTo(position.toLong())
    }

    fun setSeekBar(seekBar: SeekBar) {
        this.seekBar = seekBar
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    seekBar.max = exoPlayer.duration.toInt()
                    updateSeekBar()
                }
            }
        })
    }

    private fun updateSeekBar() {
        handler.postDelayed({
            seekBar.progress = exoPlayer.currentPosition.toInt()
            if (exoPlayer.isPlaying) {
                updateSeekBar()
            }
        }, 1000)
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}

