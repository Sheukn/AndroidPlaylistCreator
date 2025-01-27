package com.example.androidplayistcreator.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.TracksRvAdapter
import kotlin.random.Random

class TrackListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        var currentStep = 1
        var steps = mutableListOf<Step>()
        for (i in 1..10) {
            val mainTrack = Track(
                artist = "Artist $i",
                name = "Track $i",
                step = currentStep,
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                duration = "3:00",
                isSubTrack = false
            )

            val subTracks = mutableListOf<Track>()
            val random = Random.nextInt(0, 3)
            for (j in 0..random) {
                subTracks.add(
                    Track(
                        artist = "Artist $i",
                        name = "Sub Track ${i+j}",
                        step = currentStep,
                        url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                        duration = "3:00",
                        isSubTrack = true
                    )
                )
            }

            steps.add(Step(step = currentStep, mainTrack = mainTrack, subTracks = subTracks))
            currentStep++
        }

        Log.i("TrackListActivity", "Steps: $steps")
        for (step in steps) {
            Log.i("TrackListActivity", "SubTracks: ${step.subTracks.size}")
        }

        val recyclerView: RecyclerView = findViewById(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TracksRvAdapter(steps)

    }
}