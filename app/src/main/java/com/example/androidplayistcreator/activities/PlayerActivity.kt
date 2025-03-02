package com.example.androidplayistcreator.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.database.relations.PlaylistWithSteps
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.services.MusicService
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var previousButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var trackNameTextView: TextView
    private lateinit var playlistNameTextView: TextView
    private lateinit var trackArtworkImageView: ImageView

    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var currentStep: Step
    private var currentTrackIndex = 0
    private var currentTrack: TrackEntity? = null
    private var musicService: MusicService? = null
    private var serviceBound = false
    private lateinit var playlistWithSteps: PlaylistWithSteps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playButton = findViewById(R.id.playButton)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
        seekBar = findViewById(R.id.seekBar)
        trackNameTextView = findViewById(R.id.TrackNameTextView)
        playlistNameTextView = findViewById(R.id.PlaylistNameTextView)
        trackArtworkImageView = findViewById(R.id.trackArtworkImageView)

        val gson = Gson()
        val playlistJson = intent.getStringExtra("PLAYLIST_WITH_STEPS")
        playlistWithSteps = gson.fromJson(playlistJson, PlaylistWithSteps::class.java)

        playlistNameTextView.text = playlistWithSteps.playlist.name

        currentTrackIndex = 0
        currentStep = Step.fromStepsWithTracksEntity(playlistWithSteps.steps[currentTrackIndex])
        currentTrack = convertTracktoEntityTrack(currentStep.mainTrack)
        updateUI()

        enableMarqueeEffect(playlistNameTextView, playlistWithSteps.playlist.name)
        enableMarqueeEffect(trackNameTextView, currentTrack?.name ?: "")

        Glide.with(this)
            .load(currentTrack?.artwork)
            .into(trackArtworkImageView)

        val serviceIntent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
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

        previousButton.setOnClickListener {
            playPreviousTrack()
        }

        nextButton.setOnClickListener {
            playNextTrack()
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

    private fun playNextTrack() {
        if (currentTrackIndex < playlistWithSteps.steps.size - 1) {
            currentTrackIndex++
            currentStep = Step.fromStepsWithTracksEntity(playlistWithSteps.steps[currentTrackIndex])
            currentTrack = convertTracktoEntityTrack(currentStep.mainTrack)
            updateUI()
            musicService?.setTrack(currentStep.mainTrack)
            musicService?.playTrack(currentStep.mainTrack)
            isPlaying = true
            playButton.setImageResource(R.drawable.pause_button)
        }
    }

    private fun playPreviousTrack() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
            currentStep = Step.fromStepsWithTracksEntity(playlistWithSteps.steps[currentTrackIndex])
            currentTrack = convertTracktoEntityTrack(currentStep.mainTrack)
            updateUI()
            musicService?.setTrack(currentStep.mainTrack)
            musicService?.playTrack(currentStep.mainTrack)
            isPlaying = true
            playButton.setImageResource(R.drawable.pause_button)
        }
    }

    private fun updateUI() {
        trackNameTextView.text = currentTrack?.name
        enableMarqueeEffect(trackNameTextView, currentTrack?.name ?: "")
        Glide.with(this)
            .load(currentTrack?.artwork)
            .into(trackArtworkImageView)
    }

    private fun enableMarqueeEffect(textView: TextView, text: String) {
        textView.text = text
        textView.post {
            if (textView.layout != null && textView.width < textView.paint.measureText(text)) {
                textView.isSelected = true
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            serviceBound = true

            musicService?.setTrack(currentStep.mainTrack)
            musicService?.setSeekBar(seekBar)

            musicService?.playTrack(currentStep.mainTrack)
            isPlaying = true
            playButton.setImageResource(R.drawable.pause_button)

            musicService?.setOnTrackCompletionListener {
                playNextTrack()
            }
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

    private fun convertTracktoEntityTrack(track: Track): TrackEntity {
        return TrackEntity(
            id = track.id ?: 0,
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
    }
}