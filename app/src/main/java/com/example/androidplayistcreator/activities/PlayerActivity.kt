package com.example.androidplayistcreator.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.player.AudioResponse
import com.example.androidplayistcreator.serivce.YTDLPApiService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerActivity : AppCompatActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var playButton: ImageView
    private var isPlaying = false

    private val ytDlpApiService = YTDLPApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.exoPlayerView)
        playButton = findViewById(R.id.playButton)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Example YouTube URL
        val videoUrl = "https://www.youtube.com/watch?v=3BFTio5296w"

        // Fetch Audio Stream URL from Backend
        fetchAudioStream(videoUrl)

        playButton.setOnClickListener {
            if (isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            isPlaying = !isPlaying
        }
    }

    private fun fetchAudioStream(videoUrl: String) {
        val requestBody = mapOf("url" to videoUrl)

        ytDlpApiService.getAudioUrl(requestBody).enqueue(object : Callback<AudioResponse> {
            override fun onResponse(call: Call<AudioResponse>, response: Response<AudioResponse>) {
                if (response.isSuccessful) {
                    val audioUrl = response.body()?.audio_url
                    audioUrl?.let {
                        playAudio(it)
                    } ?: showToast("Failed to get audio URL.")
                } else {
                    showToast("Backend Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AudioResponse>, t: Throwable) {
                showToast("Network Error: ${t.localizedMessage}")
            }
        })
    }

    private fun playAudio(audioUrl: String) {
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        isPlaying = true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}
