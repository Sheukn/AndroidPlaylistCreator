package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.models.singletons.TrackSingleton
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.StepsRvAdapter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackListActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StepsRvAdapter
    private var playlistId: Int = 0
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var addTrackButton : ImageView
    private lateinit var startPlaylistButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        playlistId = intent.getIntExtra("PLAYLIST_ID", 0)
        playlistDao = AppDatabase.getInstance(this).playlistDao()
        recyclerView = findViewById(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StepsRvAdapter(emptyList())
        addTrackButton = findViewById(R.id.addTrackButton)
        startPlaylistButton = findViewById(R.id.startPlaylistButton)
        recyclerView.adapter = adapter
        bottomBar = findViewById(R.id.bottomBar)
        startPlaylistButton.setOnClickListener {
            startPlaylist()
        }

        setupBottomBar()
        loadSteps()
    }

    private fun setupBottomBar() {
        Log.d("PlaylistActivity", "Setting up bottom bar")
        TrackSingleton.currentTrack.observe(this) { track ->
            val recyclerView: RecyclerView = findViewById(R.id.tracksRecyclerView)
            val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
            Log.d("PlaylistActivity", "Current track changed: ${TrackSingleton.currentTrack.value}")
            if (track != null) {
                layoutParams.matchConstraintPercentHeight = 0.45f
                bottomBarController = BottomBarController(bottomBar, track.id, this)
                bottomBarController.show()
            } else {
                layoutParams.matchConstraintPercentHeight = 0.55f
                bottomBar.visibility = ConstraintLayout.GONE
            }
        }
    }


    private fun loadSteps() {
        CoroutineScope(Dispatchers.IO).launch {
            val playlistWithSteps = playlistDao.getPlaylist(playlistId)
            Log.d("TrackListActivity", "Loaded playlist: $playlistWithSteps")
            val steps = playlistWithSteps.steps?.distinctBy { it.step.id } ?: emptyList() // Filtrer par l'id de l'étape
            Log.d("TrackListActivity", "Loaded steps: ${steps.size}")
            withContext(Dispatchers.Main) {
                adapter.updateTracks(steps)
            }
        }
    }
    private fun startPlaylist() {
        if (adapter.itemCount == 0) {
            //alors vide donc on ne peut pas démarrer la lecture
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val playlistWithSteps = playlistDao.getPlaylist(playlistId)
            val gson = Gson()
            val playlistJson = gson.toJson(playlistWithSteps)

            val intent = Intent(this@TrackListActivity, PlayerActivity::class.java)
            intent.putExtra("PLAYLIST_WITH_STEPS", playlistJson)
            startActivity(intent)
        }
    }
}
