package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.database.relations.PlaylistWithSteps
import com.example.androidplayistcreator.models.Source
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.TrackSingleton
import com.example.androidplayistcreator.serivce.AudiusService
import com.example.androidplayistcreator.serivce.MusicService
import com.example.androidplayistcreator.serivce.YTDLPApiService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PlayerActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var playButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var trackNameTextView: TextView
    private lateinit var PlaylistNameTextView: TextView
    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())
    private val ytDlpApiService = YTDLPApiService.create()
    private lateinit var currentStep: Step
    private var currentTrackIndex = 0
    private var currentTrack: TrackEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val gson = Gson()
        val playlistJson = intent.getStringExtra("PLAYLIST_WITH_STEPS")
        val playlistWithSteps = gson.fromJson(playlistJson, PlaylistWithSteps::class.java)
        println("Loaded playlist: $playlistWithSteps")



        playerView = findViewById(R.id.exoPlayerView)
        playButton = findViewById(R.id.playButton)
        seekBar = findViewById(R.id.seekBar)
        trackNameTextView = findViewById(R.id.TrackNameTextView)
        PlaylistNameTextView = findViewById(R.id.PlaylistNameTextView)


        PlaylistNameTextView.text = playlistWithSteps.playlist.name
        currentStep = Step.fromStepsWithTracksEntity(playlistWithSteps.steps[0])
        trackNameTextView.text = currentStep.mainTrack.name

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Auto-play the first track
        playTrack(currentStep.mainTrack)

        playButton.setOnClickListener {
            if (isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            isPlaying = !isPlaying
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    exoPlayer.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_READY) {
                    seekBar.max = exoPlayer.duration.toInt()
                    updateSeekBar()
                } else if (state == ExoPlayer.STATE_ENDED) {
                    //launch new truc
                }
            }
        })
    }

    private fun playTrack(track: Track) {
        trackNameTextView.text = track.name
        val trackEntity = TrackEntity(
            id = track.id ?: 0, // Ensure id is not null
            name = track.name,
            artist = track.artist ?: "Unknown",
            video_id = track.video_id,
            source = track.source ?: "AUDIUS",
            duration = track.duration,
            isSubTrack = track.isSubTrack,
            stepId = track.step
        )
        TrackSingleton.setCurrentTrack(trackEntity)
        fetchAudioStream(track.video_id, track.source ?: "AUDIUS")
    }

    private fun fetchAudioStream(videoId: String, source: String) {
        if (source == Source.AUDIUS.toString()) {
            lifecycleScope.launch {
                try {
                    val track = AudiusService.api.getTrack(videoId)
                    val audioUrl = AudiusService.getStreamUrl(track.data.id)
                    playAudio(audioUrl)
                } catch (e: Exception) {
                    showToast("Failed to fetch audio stream from Audius, error: $e")
                }
            }
        } else {
            showToast("Unsupported source: $source")
        }
    }

    private fun playAudio(audioUrl: String) {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("AUDIO_URL", audioUrl)
        }
        startService(intent)
    }


    private fun updateSeekBar() {
        handler.postDelayed({
            seekBar.progress = exoPlayer.currentPosition.toInt()
            if (exoPlayer.isPlaying) {
                updateSeekBar()
            }
        }, 1000)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackSingleton.clearTrack()
        exoPlayer.release()
    }
}
