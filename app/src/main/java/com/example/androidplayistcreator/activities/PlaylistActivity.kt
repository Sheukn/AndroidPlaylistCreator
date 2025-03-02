package com.example.androidplayistcreator.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.DatabaseMigrator
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.models.singletons.TrackSingleton
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
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeChangeButton: ToggleButton
    private lateinit var deleteButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the theme
        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_playlist)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()
        userTextView = findViewById(R.id.userTextView)
        createPlaylistButton = findViewById(R.id.createPlaylistButton)
        bottomBar = findViewById(R.id.bottomBar)
        deleteButton = findViewById(R.id.deleteAllButton)

        createPlaylistButton.setOnClickListener {
            val intent = Intent(this, PlaylistCreatorActivity::class.java)
            startActivity(intent)
        }
        trackSingleton = TrackSingleton
        setupBottomBar()

        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val migrator = DatabaseMigrator(this@PlaylistActivity)
                migrator.clearDatabase()
                withContext(Dispatchers.Main) {
                    loadPlaylists()
                }
            }
        }

        val themeChangeButton: ToggleButton = findViewById(R.id.themeChangeButton)
        themeChangeButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkTheme", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkTheme", true)
            }
            editor.apply()
            recreate() // Recreate the activity to apply the new theme
        }

        CoroutineScope(Dispatchers.IO).launch {
//            val migrator = DatabaseMigrator(this@PlaylistActivity)
//            migrator.clearDatabase()
//            Log.d ("PlaylistActivity", "PlaylistDao: ${playlistDao.getAllTracks()}")
//            migrator.migrate()
//            Log.d ("PlaylistActivity", "PlaylistDao: ${playlistDao.getAllTracks()}")
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
        Log.d("PlaylistActivity", "Setting up bottom bar")
        TrackSingleton.currentTrack.observe(this) { track ->
            Log.d("PlaylistActivity", "Current track changed: ${TrackSingleton.currentTrack.value}")
            val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
            val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
            if (track != null) {
                layoutParams.matchConstraintPercentHeight = 0.35f
                bottomBarController = BottomBarController(bottomBar, track.id, this)
                bottomBarController.show()
            } else {
                layoutParams.matchConstraintPercentHeight = 0.5f
                bottomBar.visibility = ConstraintLayout.GONE
            }
        }
    }
    private fun updateRecyclerView(playlists: List<PlaylistEntity>) {
        val recyclerView: RecyclerView = findViewById(R.id.playlist_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistsRvAdapter(playlists)  // No need to pass com.example.androidplayistcreator.models.singletons.TrackSingleton
    }

    override fun onResume() {
        super.onResume()
        loadPlaylists()
    }
}
