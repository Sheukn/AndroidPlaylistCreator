package com.example.androidplayistcreator.utils

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

object WebViewUtil {
    @SuppressLint("SetJavaScriptEnabled")
    fun loadYouTubeVideo(container: FrameLayout, videoId: String) {
        val webView = WebView(container.context)

        webView.clearCache(true)

        // Disable cache
        webView.settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE

        // Enable JavaScript and other necessary settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        // Set WebViewClient and WebChromeClient
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        val url =
            "https://music.youtube.com/embed/$videoId?si=esFxWgn_VU7xXXQg" +
                    "&autoplay=1&controls=0&modestbranding=1&playsinline=1"
//        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        webView.loadUrl(url)

        // Add WebView to container
        container.removeAllViews()  // Remove any previous WebView
        container.addView(webView)
    }
}