package com.example.androidplayistcreator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.SearchResultRvAdapter

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)

        val result = mutableListOf<Track>()

        // Sample data for search results
        val COUNT = 10
        for (i in 1..COUNT) {
            if (i % 2 == 0) {
                result.add(
                    Track(
                        artist = "Artist $i",
                        name = "Track $i",
                        step = 1,
                        url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                        duration = "3:00",
                        isSubTrack = true,
                        source = "SPOTIFY",
                    )
                )
                continue
            }
            result.add(
                Track(
                    artist = "Artist $i",
                    name = "Track $i",
                    step = 1,
                    url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                    duration = "3:00",
                    isSubTrack = false
                )
            )
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SearchResultRvAdapter(result)
    }
}