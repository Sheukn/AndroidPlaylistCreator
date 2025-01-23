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

        // Sample data for playlists
        val playlists = listOf(
            Playlist(
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
            ),
            Playlist(
                condition = listOf(
                    Condition("Destination 3", "Condition 3", "Source 3", ConditionType.IF)
                ),
                creator = "Creator 2",
                id = 2,
                name = "Playlist 2",
                tracks = listOf(),
                url = "https://example.com/playlist2",
                imageUrl = "https://example.com/playlist2.jpg"
            ),
            Playlist(
                condition = listOf(
                    Condition("Destination 3", "Condition 3", "Source 3", ConditionType.IF),
                    Condition("Destination 1", "Condition 1", "Source 1", ConditionType.IF),
                    Condition("Destination 2", "Condition 2", "Source 2", ConditionType.IF)
                ),
                creator = "Creator 2",
                id = 2,
                name = "Playlist 2",
                tracks = listOf(),
                url = "https://example.com/playlist2",
                imageUrl = "https://example.com/playlist2.jpg"
            )
        )

        // RecyclerView setup
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists = playlists)
    }
}
