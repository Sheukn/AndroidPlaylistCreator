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
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.singletons.PlaylistSingleton
import com.example.androidplayistcreator.models.singletons.TrackSingleton
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.CreatePlaylistStepRVAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
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
        playlistNameEditText = findViewById(R.id.playlistNameEditText)

        playlistCreateRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CreatePlaylistStepRVAdapter(stepsList)
        playlistCreateRecyclerView.adapter = adapter

        addStepButton.setOnClickListener {
            addStep()
        }

        createPlaylistButton.setOnClickListener {
            CoroutineScope (Dispatchers.IO).launch {
                createPlaylist()
            }
            finish()
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

    private suspend fun createPlaylist() {
        var newPlaylist = PlaylistEntity(
            name = playlistNameEditText.text.toString(),
            imageUrl = null
        )
        playlistDao.insertPlaylist(newPlaylist);
        var playlistId = playlistDao.getPlaylistIdByName(newPlaylist.name)
        var steps = mutableListOf<StepEntity>()
        var tracks = mutableListOf<TrackEntity>()
        var cpt = 0

        for(step in stepsList){
            steps.add(StepEntity(
                playlistId = playlistId,
                step = cpt
            ))
            Log.d("PlaylistCreatorActivity", "Step added: $cpt")
            cpt++
        }
        playlistDao.insertSteps(steps)

        var dbSteps = playlistDao.getStepsByPlaylistId(playlistId)
        Log.d("PlaylistCreatorActivity", "dbSteps: ${dbSteps}")
        cpt = 0

        for(step in stepsList) {

            tracks.add(TrackEntity(
                id = cpt,
                artist = step.mainTrack.artist?:"unknown",
                name = step.mainTrack.name,
                stepId = dbSteps[cpt].id,
                audiusId = step.mainTrack.audiusId,
                duration = step.mainTrack.duration,
                isSubTrack = false,
                source = step.mainTrack.source,
                isStreamable = step.mainTrack.isStreamable,
                artwork = step.mainTrack.artwork
            ))
            Log.d("PlaylistCreatorActivity", "Main track added: ${step.mainTrack.name}")
            for(subTrack in step.subTracks) {
                tracks.add(
                    TrackEntity(
                        id = cpt,
                        artist = subTrack.artist?:"unknown",
                        name = subTrack.name,
                        stepId = dbSteps[cpt].id,
                        audiusId = subTrack.audiusId,
                        duration = subTrack.duration,
                        isSubTrack = true,
                        source = subTrack.source,
                        isStreamable = subTrack.isStreamable,
                        artwork = subTrack.artwork
                    )
                )
                Log.d("PlaylistCreatorActivity", "Subtrack added: ${subTrack.name}")
            }
            cpt ++
        }
        playlistDao.insertTracks(tracks)

    }

}

