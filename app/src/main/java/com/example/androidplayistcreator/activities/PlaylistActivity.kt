package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.DatabaseMigrator
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.models.TrackSingleton
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.PlaylistsRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var userTextView: TextView
    private lateinit var createPlaylistButton: ImageView
    private lateinit var trackSingleton: TrackSingleton
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()

        userTextView = findViewById(R.id.userTextView)
        createPlaylistButton = findViewById(R.id.createPlaylistButton)
        bottomBar = findViewById(R.id.bottomBar)

        createPlaylistButton.setOnClickListener {
            val intent = Intent(this, PlaylistCreatorActivity::class.java)
            startActivity(intent)
        }
        trackSingleton = TrackSingleton

        CoroutineScope(Dispatchers.IO).launch {
            val migrator = DatabaseMigrator(this@PlaylistActivity)
            migrator.clearDatabase()
            migrator.migrate()
            setupBottomBar()
            loadPlaylists()
        }
    }

    private fun loadPlaylists() {
        CoroutineScope(Dispatchers.IO).launch {
            val playlists = playlistDao.getAllPlaylists()
            val id = 1
            val steps = playlistDao.getStepsByPlaylistId(id)
            Log.d("PlaylistActivity", "Steps for playlist $id: ${steps.size}")
            for (step in steps) {
                val tracks = playlistDao.getTracksByStepId(step.id)
                Log.d("PlaylistActivity", "Tracks for step ${step.id}: ${tracks.size}")
            }
            withContext(Dispatchers.Main) {
                updateRecyclerView(playlists)
            }
        }
    }

    private fun setupBottomBar() {
        if (TrackSingleton.getCurrentTrackId() != 0) {
            bottomBarController = BottomBarController(bottomBar, TrackSingleton.getCurrentTrackId(), this)
            bottomBarController.show()
        } else {
            bottomBar.visibility = ConstraintLayout.GONE
        }
    }

    private fun updateRecyclerView(playlists: List<PlaylistEntity>) {
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists)  // No need to pass TrackSingleton
    }
}
