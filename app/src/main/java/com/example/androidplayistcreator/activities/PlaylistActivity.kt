package com.example.androidplayistcreator.activities

import PlaylistRepository
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.views.recycler_view_adapters.PlaylistsRvAdapter

class PlaylistActivity : AppCompatActivity() {
    private lateinit var userTextView: TextView
    private lateinit var createPlaylistButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        userTextView = findViewById(R.id.userTextView)
        createPlaylistButton = findViewById(R.id.createPlaylistButton)

        createPlaylistButton.setOnClickListener {
            val intent = Intent(this, PlaylistCreatorActivity::class.java)
            startActivity(intent)
        }

        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        val playlists = PlaylistRepository(this).getAllPlaylists()
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists = playlists)
    }
}