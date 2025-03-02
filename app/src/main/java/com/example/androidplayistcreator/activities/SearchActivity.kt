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
import com.example.androidplayistcreator.models.singletons.PlaylistSingleton
import com.example.androidplayistcreator.models.singletons.TrackSingleton
import com.example.androidplayistcreator.services.AudiusService
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.SearchResultRvAdapter
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val TAG = "SearchActivity"
    private val SOURCE_AUDIUS = "Audius"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultRvAdapter
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var audiusCheckBox: CheckBox
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout

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
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultRvAdapter(mutableListOf()) { track ->
            Log.d("SearchActivity", "User selected: ${track.name} by ${track.artist}")

            val isMainTrack = intent.getBooleanExtra("isMainTrack", false)
            val currentStepPosition = PlaylistSingleton.currentStepPosition

            if (trackExistsInPlaylist(track, isMainTrack)) {
                showAlert("Track already exists", "This track is already in the playlist")
                return@SearchResultRvAdapter
            }

            if (currentStepPosition != -1) {
                PlaylistSingleton.playlist.steps[currentStepPosition].also { step ->
                    if (isMainTrack) {
                        step.mainTrack = track
                    } else {
                        step.subTracks.add(track)
                    }
                }
            }

            setResult(Activity.RESULT_OK)
            finish()
        }

        recyclerView.adapter = adapter
    }
    private fun setupListeners() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        val allResults = mutableListOf<Track>()
        searchAudius(query) { audiusResults ->
            allResults.addAll(audiusResults)
            updateRecyclerViewIfFinished(allResults)
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

    private fun searchAudius(query: String, callback: (List<Track>) -> Unit) {
        lifecycleScope.launch {
            try {
                val response = AudiusService.api.searchTracks(query)
                Log.d(TAG, "Audius search response: $response")
                val searchResults = response.data.map { track ->
                    Track(
                        artist = track.artist?.name ?: "Unknown",
                        name = track.title,
                        step = 1,
                        audiusId = track.id,
                        duration = "0",
                        isSubTrack = false,
                        source = SOURCE_AUDIUS,
                        isStreamable = track.isStreamble,
                        artwork = track.artwork?.`1000x1000` ?: track.artwork?.`480x480`
                        ?: track.artwork?.`150x150` ?: "No artwork"
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

    private fun trackExistsInPlaylist(track: Track, isMainTrack: Boolean): Boolean {
        return if (isMainTrack) {
            PlaylistSingleton.playlist.steps.any { it.mainTrack.audiusId == track.audiusId }
        } else {
            PlaylistSingleton.playlist.steps.any { step ->
                step.subTracks.any { it.audiusId == track.audiusId }
            }
        }
    }
}

