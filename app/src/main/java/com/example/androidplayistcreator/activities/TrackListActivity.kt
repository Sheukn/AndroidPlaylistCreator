package com.example.androidplayistcreator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.StepsRvAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        playlistId = intent.getIntExtra("PLAYLIST_ID", 0)

        playlistDao = AppDatabase.getInstance(this).playlistDao()

        recyclerView = findViewById(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StepsRvAdapter(emptyList())
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.addTrackButton).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TRACK)
        }
        findViewById<FloatingActionButton>(R.id.startPlaylistButton).setOnClickListener {
            startPlaylist()
        }


        loadSteps()
    }

    private fun loadSteps() {
        CoroutineScope(Dispatchers.IO).launch {
            val playlistWithSteps = playlistDao.getPlaylist(playlistId)
            Log.d("TrackListActivity", "Loaded playlist: $playlistWithSteps")
            val steps = playlistWithSteps.steps.distinctBy { it.step.id } ?: emptyList() // Filtrer par l'id de l'étape
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TRACK && resultCode == RESULT_OK) {
            loadSteps()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_TRACK = 1
    }
}
