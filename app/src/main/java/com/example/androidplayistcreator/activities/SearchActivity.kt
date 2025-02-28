package com.example.androidplayistcreator.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.TrackSingleton
import com.example.androidplayistcreator.models.player.SearchResponse
import com.example.androidplayistcreator.services.AudiusService
import com.example.androidplayistcreator.services.YTDLPApiService
import com.example.androidplayistcreator.views.BottomBarController
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
    private lateinit var audiusCheckBox: CheckBox
    private lateinit var youtubeCheckBox: CheckBox
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout

    private val apiService = YTDLPApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)
        bottomBar = findViewById(R.id.bottomBar)
        setupViews()
        setupListeners()
        setupBottomBar()
    }

    private fun setupBottomBar() {
        Log.d("PlaylistActivity", "Setting up bottom bar")
        TrackSingleton.currentTrack.observe(this) { track ->
            Log.d("PlaylistActivity", "Current track changed: ${TrackSingleton.currentTrack.value}")
            if (track != null) {
                bottomBarController = BottomBarController(bottomBar, track.id, this)
                bottomBarController.show()
            } else {
                bottomBar.visibility = ConstraintLayout.GONE
            }
        }
    }

    private fun setupViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        audiusCheckBox = findViewById(R.id.audiusCheckBox)
        youtubeCheckBox = findViewById(R.id.youtubeCheckBox)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultRvAdapter(mutableListOf()) { track ->
            Log.d("SearchActivity", "User selected: ${track.name} by ${track.artist}")
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("SELECTED_TRACK", track.name)
            })
//            finish()
        }

        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                val selectedSources = getSelectedSources()
                if (selectedSources.isNotEmpty()) {
                    performSearch(query, selectedSources)
                } else {
                    showAlert("Search failed", "Please select at least one source.")
                }
            } else {
                showAlert("Search failed", "Please enter a search query.")
            }
        }
    }

    private fun getSelectedSources(): List<String> {
        val sources = mutableListOf<String>()
        if (audiusCheckBox.isChecked) sources.add(SOURCE_AUDIUS)
        if (youtubeCheckBox.isChecked) sources.add(SOURCE_YOUTUBE)
        return sources
    }

    private fun performSearch(query: String, sources: List<String>) {
        val allResults = mutableListOf<Track>()

        val youtubeSearch = sources.contains(SOURCE_YOUTUBE)
        val audiusSearch = sources.contains(SOURCE_AUDIUS)

        if (youtubeSearch) {
            searchYouTube(query) { youtubeResults ->
                allResults.addAll(youtubeResults)
                updateRecyclerViewIfFinished(allResults)
            }
        }

        if (audiusSearch) {
            searchAudius(query) { audiusResults ->
                allResults.addAll(audiusResults)
                updateRecyclerViewIfFinished(allResults)
            }
        }
    }

    private fun updateRecyclerViewIfFinished(
        results: List<Track>,
    ) {
        if (results.isNotEmpty()) {
            adapter.updateTracks(results)
        } else {
            showAlert("Search failed", "Nothing Found")
        }
    }

    private fun searchYouTube(query: String, callback: (List<Track>) -> Unit) {
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

                    callback(searchResults)
                } else {
                    showAlert("Search failed", "Unable to fetch search results from YouTube.")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "YouTube search failed", t)
                showAlert("Search failed", "Unable to fetch search results on YouTube.")
                callback(emptyList())
            }
        })
    }

    private fun searchAudius(query: String, callback: (List<Track>) -> Unit) {
        lifecycleScope.launch {
            try {
                val response = AudiusService.api.searchTracks(query)
                val searchResults = response.data.map { track ->
                    Track(
                        artist = track.artist.name,
                        name = track.title,
                        step = 1,
                        video_id = track.id,
                        duration = "0",
                        isSubTrack = false,
                        source = SOURCE_AUDIUS
                    )
                }
                callback(searchResults)
            } catch (e: Exception) {
                Log.e(TAG, "Audius search failed", e)
                showAlert("Search failed", "Unable to fetch search results on Audius.")
                callback(emptyList())
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

