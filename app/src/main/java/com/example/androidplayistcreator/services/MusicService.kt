package com.example.androidplayistcreator.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidplayistcreator.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MusicService : Service() {
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()

        startForeground(NOTIFICATION_ID, createNotification())

        exoPlayer.addListener(object : com.google.android.exoplayer2.Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_ENDED) {
                    stopSelf()
                }
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val audioUrl = intent?.getStringExtra("AUDIO_URL")
        if (!audioUrl.isNullOrEmpty()) {
            playAudio(audioUrl)
        }
        return START_STICKY
    }

    private fun playAudio(audioUrl: String) {
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }


    private fun createNotification(): Notification {
        val channelId = "MusicServiceChannel"
        val channel = NotificationChannel(
            channelId,
            "Music Playback",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Lecture en cours")
            .setContentText("Votre musique joue...")
            .setSmallIcon(R.drawable.ic_music_note)
            .build()
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }


    companion object {
        private const val NOTIFICATION_ID = 1
    }
}
