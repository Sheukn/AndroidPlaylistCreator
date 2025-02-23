package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.TrackRepository
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.TracksRvAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrackListActivity : AppCompatActivity() {
    private lateinit var trackRepository: TrackRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TracksRvAdapter
    private var playlistId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        playlistId = intent.getIntExtra("PLAYLIST_ID", 0)
        trackRepository = TrackRepository(this)

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
        val tracks = trackRepository.getTracksByPlaylistId(playlistId)
        adapter.updateTracks(tracks)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TRACK && resultCode == RESULT_OK) {
            loadTracks()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_TRACK = 1
    }
}