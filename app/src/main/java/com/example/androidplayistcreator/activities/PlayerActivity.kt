package com.example.androidplayistcreator.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.database.relations.PlaylistWithSteps
import com.example.androidplayistcreator.models.Source
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.TrackSingleton
import com.example.androidplayistcreator.services.AudiusService
import com.example.androidplayistcreator.services.MusicService
import com.example.androidplayistcreator.services.YTDLPApiService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PlayerActivity : AppCompatActivity() {
    private lateinit var playButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var trackNameTextView: TextView
    private lateinit var PlaylistNameTextView: TextView
    private lateinit var trackArtworkImageView: ImageView

    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var currentStep: Step
    private var currentTrackIndex = 0
    private var currentTrack: TrackEntity? = null
    private var musicService: MusicService? = null
    private var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val gson = Gson()
        val playlistJson = intent.getStringExtra("PLAYLIST_WITH_STEPS")
        val playlistWithSteps = gson.fromJson(playlistJson, PlaylistWithSteps::class.java)
        println("Loaded playlist: $playlistWithSteps")

        playButton = findViewById(R.id.playButton)
        seekBar = findViewById(R.id.seekBar)
        trackNameTextView = findViewById(R.id.TrackNameTextView)
        PlaylistNameTextView = findViewById(R.id.PlaylistNameTextView)
        trackArtworkImageView = findViewById(R.id.trackArtworkImageView)

        PlaylistNameTextView.text = playlistWithSteps.playlist.name
        currentStep = Step.fromStepsWithTracksEntity(playlistWithSteps.steps[0])
        trackNameTextView.text = currentStep.mainTrack.name

        PlaylistNameTextView.post {
            if (PlaylistNameTextView.layout != null && PlaylistNameTextView.width < PlaylistNameTextView.paint.measureText(playlistWithSteps.playlist.name)) {
                PlaylistNameTextView.isSelected = true  // Ensures marquee effect starts
            }
        }

        trackNameTextView.post {
            if (trackNameTextView.layout != null && trackNameTextView.width < trackNameTextView.paint.measureText(currentStep.mainTrack.name)) {
                trackNameTextView.isSelected = true  // Ensures marquee effect starts
            }
        }

        Glide.with(this)
            .load(currentStep.mainTrack.artwork)
            .into(trackArtworkImageView)

        // Bind to MusicService to handle playback
        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        playButton.setOnClickListener {
            if (isPlaying) {
                playButton.setImageResource(R.drawable.play_button)
                musicService?.pauseTrack()
            } else {
                playButton.setImageResource(R.drawable.pause_button)
                musicService?.playTrack(currentStep.mainTrack)
            }
            isPlaying = !isPlaying
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            serviceBound = true

            // Set initial state
            musicService?.setTrack(currentStep.mainTrack)
            musicService?.setSeekBar(seekBar)

            // Start playing the first track
            musicService?.playTrack(currentStep.mainTrack)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            serviceBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
    }
}

