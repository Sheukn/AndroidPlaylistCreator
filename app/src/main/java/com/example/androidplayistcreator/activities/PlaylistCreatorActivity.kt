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
import com.example.androidplayistcreator.models.TrackSingleton
import com.example.androidplayistcreator.views.BottomBarController
import com.example.androidplayistcreator.views.recycler_view_adapters.CreatePlaylistStepRVAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistCreatorActivity : AppCompatActivity() {
    private lateinit var playlistDao: PlaylistDao
    private lateinit var playlistNameEditText: EditText
    private lateinit var addTrackButton: ImageView
    private lateinit var createPlaylistButton: Button
    private lateinit var bottomBarController: BottomBarController
    private lateinit var bottomBar: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        // Initialize Room database and DAO
        playlistDao = AppDatabase.getInstance(this).playlistDao()

        playlistNameEditText = findViewById(R.id.playlistNameEditText)
        addTrackButton = findViewById(R.id.addTrackButton)
        createPlaylistButton = findViewById(R.id.playlistCreateButton)
        bottomBar = findViewById(R.id.bottomBar)

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
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.playlistCreateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CreatePlaylistStepRVAdapter(emptyList())
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
