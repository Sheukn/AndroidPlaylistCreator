package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.views.recycler_view_adapters.PlaylistsRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var userTextView: TextView
    private lateinit var createPlaylistButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()

        userTextView = findViewById(R.id.userTextView)
        createPlaylistButton = findViewById(R.id.createPlaylistButton)

        createPlaylistButton.setOnClickListener {
            val intent = Intent(this, PlaylistCreatorActivity::class.java)
            startActivity(intent)
        }

        // Load playlists from the database
        loadPlaylists()
    }

    private fun loadPlaylists() {
        // Fetch playlists from the database in a background thread using coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val playlists = playlistDao.getAllPlaylists()  // Fetch all playlists from Room database

            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                updateRecyclerView(playlists)
            }
        }
    }

    private fun updateRecyclerView(playlists: List<PlaylistEntity>) {
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists = playlists)
    }
}
