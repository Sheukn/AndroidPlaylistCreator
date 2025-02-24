package com.example.androidplayistcreator.activities

import PlaylistRepository
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Playlist
import com.example.androidplayistcreator.views.recycler_view_adapters.CreatePlaylistStepRVAdapter

class PlaylistCreatorActivity : AppCompatActivity() {
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var playlistNameEditText: EditText
    private lateinit var addTrackButton: ImageView
    private lateinit var createPlaylistButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        playlistRepository = PlaylistRepository(this)
        playlistNameEditText = findViewById(R.id.playlistNameEditText)
        addTrackButton = findViewById(R.id.addTrackButton)
        createPlaylistButton = findViewById(R.id.playlistCreateButton)

        createPlaylistButton.setOnClickListener {
            val playlistName = playlistNameEditText.text.toString()
            if (playlistName.isNotEmpty()) {
                val newPlaylist = Playlist(emptyList(), 0, playlistName, emptyList(), null)
                playlistRepository.addPlaylist(newPlaylist)
                finish()
            }
        }

        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.playlistCreateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CreatePlaylistStepRVAdapter(emptyList())
    }
}