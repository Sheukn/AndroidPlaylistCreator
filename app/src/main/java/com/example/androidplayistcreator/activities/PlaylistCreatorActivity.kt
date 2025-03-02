package com.example.androidplayistcreator.activities

import android.os.Bundle
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

    private val stepsList = mutableListOf<Step>()

    private lateinit var adapter: CreatePlaylistStepRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()

        playlistNameEditText = findViewById(R.id.playlistNameEditText)
        addStepButton = findViewById(R.id.addStepButton)
        createPlaylistButton = findViewById(R.id.playlistCreateButton)
        bottomBar = findViewById(R.id.bottomBar)

        // Set up RecyclerView and adapter
        val recyclerView: RecyclerView = findViewById(R.id.playlistCreateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CreatePlaylistStepRVAdapter(stepsList)
        recyclerView.adapter = adapter

        // Set up addTrackButton click listener
        addStepButton.setOnClickListener {
            addStep()
        }

        // Set up createPlaylistButton click listener
        createPlaylistButton.setOnClickListener {
            val playlistName = playlistNameEditText.text.toString()
            if (playlistName.isNotEmpty()) {
                val newPlaylist = PlaylistEntity(id = 0, name = playlistName, imageUrl = null)

                CoroutineScope(Dispatchers.IO).launch {
                    val playlistId = playlistDao.insertPlaylist(newPlaylist).toInt()

                    val steps = listOf(
                        StepEntity(step = 1, playlistId = playlistId),
                        StepEntity(step = 2, playlistId = playlistId)
                    )

                    playlistDao.insertSteps(steps)

                    withContext(Dispatchers.Main) {
                        finish()
                    }
                }
            }
        }

        setupBottomBar()
    }

    // Function to add a new step to the list
    private fun addStep() {
        stepsList.add(Step(mainTrack = Track(name = "Track", artist = "Artist", audiusId = "0", duration = "0", isStreamable = false, step = 0, isSubTrack = false, source = null, artwork = null), subTracks = mutableListOf()))
        adapter.notifyItemInserted(stepsList.size - 1)
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

