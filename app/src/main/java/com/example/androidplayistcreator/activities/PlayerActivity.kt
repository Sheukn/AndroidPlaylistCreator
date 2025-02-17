package com.example.androidplayistcreator.activities

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidplayistcreator.BuildConfig
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.serivce.YouTubeApiService
import com.example.androidplayistcreator.utils.WebViewUtil

class PlayerActivity   : AppCompatActivity() {
    private var isPlaying = false
    private lateinit var playButton: ImageView
    private lateinit var youtubeWebViewContainer: FrameLayout

    private val youtubeApiKey by lazy { BuildConfig.YOUTUBE_API_KEY }
    private val youtubeApiService = YouTubeApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playButton = findViewById(R.id.playButton)

        val youtubeWebViewContainer = findViewById<FrameLayout>(R.id.youtubeWebViewContainer)

        // The YouTube video ID to play
        val videoId = "3BFTio5296w"  // The video you provided
//        "https://m.youtube.com/v/3BFTio5296w?autoplay=1&controls=0&modestbranding=1&playsinline=1"


        // Load the video
        WebViewUtil.loadYouTubeVideo(youtubeWebViewContainer, videoId)

        playButton.setOnClickListener {
            if (isPlaying) {
                // Pause the video by stopping WebView (or use JavaScript)
                stopVideo()
            } else {
                // Play the video by loading the WebView again (autoplay)
                startVideo(videoId)
            }
            isPlaying = !isPlaying
        }

    }

    private fun startVideo(videoId: String) {
        WebViewUtil.loadYouTubeVideo(youtubeWebViewContainer, videoId)
    }

    private fun stopVideo() {
        youtubeWebViewContainer.removeAllViews()
    }

    private fun searchYouTube(query: String, onResult: (String?) -> Unit) {
        youtubeApiService.searchYouTube(query, youtubeApiKey, onResult)
    }

    private fun loadYouTubeVideo(container: FrameLayout, videoId: String) {
        WebViewUtil.loadYouTubeVideo(container, videoId)
    }
}