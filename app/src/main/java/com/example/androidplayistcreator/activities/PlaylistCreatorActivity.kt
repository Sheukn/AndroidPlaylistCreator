package com.example.androidplayistcreator.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.AppDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.database.entities.StepEntity
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.singletons.PlaylistSingleton
import com.example.androidplayistcreator.models.singletons.TrackSingleton
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.CreatePlaylistStepRVAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistCreatorActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var playlistNameEditText: EditText
    private lateinit var addStepButton: ImageView
    private lateinit var createPlaylistButton: Button
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var playlistCreateRecyclerView: RecyclerView

    private val stepsList: MutableList<Step>
        get() = PlaylistSingleton.playlist.steps

    private lateinit var adapter: CreatePlaylistStepRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        playlistDao = AppDatabase.getInstance(this).playlistDao()
        playlistCreateRecyclerView = findViewById(R.id.playlistCreateRecyclerView)
        addStepButton = findViewById(R.id.addStepButton)
        createPlaylistButton = findViewById(R.id.playlistCreateButton)
        bottomBar = findViewById(R.id.bottomBar)

        playlistCreateRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CreatePlaylistStepRVAdapter(stepsList)
        playlistCreateRecyclerView.adapter = adapter

        addStepButton.setOnClickListener {
            addStep()
        }

        createPlaylistButton.setOnClickListener {
            // Log current data in the singleton
            Log.d("PlaylistCreatorActivity", "Playlist: ${PlaylistSingleton.playlist}")

        }

        setupBottomBar()
    }

    private fun addStep() {
        stepsList.add(
            Step(
                mainTrack = Track(name = "Track", artist = "Artist", audiusId = "0", duration = "0", isStreamable = false, step = 0, isSubTrack = false, source = null, artwork = null),
                subTracks = mutableListOf() // Initialize as MutableList
            )
        )
        adapter.notifyItemInserted(stepsList.size - 1)
    }

    override fun onResume() {
        super.onResume()
        // Refresh the adapter when the activity resumes
        adapter.notifyDataSetChanged()
    }

    private fun setupBottomBar() {
        if (TrackSingleton.getCurrentTrackId() != 0) {
            bottomBarController = BottomBarController(bottomBar, TrackSingleton.getCurrentTrackId(), this)
            bottomBarController.show()
        } else {
            bottomBar.visibility = ConstraintLayout.GONE
        }
    }


}

