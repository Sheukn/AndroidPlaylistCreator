package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.PlaylistDao
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.TracksRvAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackListActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TracksRvAdapter
    private var playlistId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        playlistId = intent.getIntExtra("PLAYLIST_ID", 0)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()

        recyclerView = findViewById(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TracksRvAdapter(emptyList())
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.addTrackButton).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TRACK)
        }

        loadTracks()
    }

    private fun loadTracks() {
        // Use Room DAO to get tracks for the given playlist ID
        CoroutineScope(Dispatchers.IO).launch {
            val playlistWithSteps = playlistDao.getPlaylistWithSteps(playlistId)
            val tracks = playlistWithSteps?.steps?.flatMap { it.tracks } ?: emptyList()

            // Update UI with tracks in the main thread
            withContext(Dispatchers.Main) {
                adapter.updateTracks(tracks)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TRACK && resultCode == RESULT_OK) {
            loadTracks()  // Refresh track list after adding a new track
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_TRACK = 1
    }
}
