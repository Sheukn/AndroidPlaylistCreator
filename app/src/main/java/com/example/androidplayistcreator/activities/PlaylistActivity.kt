package com.example.androidplayistcreator.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Condition
import com.example.androidplayistcreator.models.ConditionType
import com.example.androidplayistcreator.models.Playlist
import com.example.androidplayistcreator.views.recycler_view_adapters.PlaylistsRvAdapter

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        var playlist = Playlist(
            condition = listOf(
                Condition("Destination 1", "Condition 1", "Source 1", ConditionType.IF),
                Condition("Destination 2", "Condition 2", "Source 2", ConditionType.IF)
            ),
            creator = "Creator 1",
            id = 1,
            name = "Playlist 1",
            tracks = listOf(),
            url = "https://example.com/playlist1",
            imageUrl = "https://example.com/playlist1.jpg"
        )

        // Sample data for playlists
        val COUNT = 10
        val playlists = List(COUNT) { playlist }

        // RecyclerView setup
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists = playlists)
    }
}
