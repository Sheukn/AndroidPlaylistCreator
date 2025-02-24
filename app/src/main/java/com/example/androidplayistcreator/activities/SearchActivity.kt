package com.example.androidplayistcreator.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.player.SearchResponse
import com.example.androidplayistcreator.serivce.AudiusService
import com.example.androidplayistcreator.serivce.YTDLPApiService
import com.example.androidplayistcreator.views.recycler_view_adapters.SearchResultRvAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private val TAG = "SearchActivity"
    private val SOURCE_YOUTUBE = "YouTube"
    private val SOURCE_AUDIUS = "Audius"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultRvAdapter
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var sourceButton: Button

    private val apiService = YTDLPApiService.create()
    private var selectedSource = SOURCE_AUDIUS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        sourceButton = findViewById(R.id.sourceButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SearchResultRvAdapter(mutableListOf()) { track ->
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("SELECTED_TRACK", track.toString())
            })
            finish()
        }
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        sourceButton.setOnClickListener {
            selectedSource = if (selectedSource == SOURCE_AUDIUS) SOURCE_YOUTUBE else SOURCE_AUDIUS
            Log.d(TAG, "Selected source: $selectedSource")
            sourceButton.text = selectedSource
        }

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query, selectedSource)
            } else {
                showAlert("Search failed", "Please enter a search query.")
            }
        }
    }

    private fun performSearch(query: String, source: String) {
        when (source) {
            SOURCE_YOUTUBE -> searchYouTube(query)
            SOURCE_AUDIUS -> searchAudius(query)
        }
    }

    private fun searchYouTube(query: String) {
        apiService.searchYoutubeVideos(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.results?.map {
                        Track(
                            artist = it.artist,
                            name = it.title,
                            step = 1,
                            video_id = it.url.substringAfter("v=").substringBefore("&"),
                            duration = it.duration,
                            isSubTrack = false,
                            source = SOURCE_YOUTUBE
                        )
                    } ?: emptyList()

                    adapter.updateTracks(searchResults)
                } else {
                    showAlert("Search failed", "Unable to fetch search results.")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "YouTube search failed", t)
                showAlert("Search failed", "Unable to fetch search results on YouTube.")
            }
        })
    }

    private fun searchAudius(query: String) {
        lifecycleScope.launch {
            try {
                val response = AudiusService.api.searchTracks(query)
                val searchResults = response.data.map { track ->
                    Track(
                        artist = track.user.name,
                        name = track.title,
                        step = 1,
                        video_id = track.id,
                        duration = track.duration.toString(),
                        isSubTrack = false,
                        source = SOURCE_AUDIUS
                    )
                }
                adapter.updateTracks(searchResults)
                Log.d(TAG, "Search results: $searchResults")
            } catch (e: Exception) {
                Log.e(TAG, "Audius search failed", e)
                showAlert("Search failed", "Unable to fetch search results on Audius.")
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

}
