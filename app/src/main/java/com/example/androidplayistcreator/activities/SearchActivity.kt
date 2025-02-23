package com.example.androidplayistcreator.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.models.player.SearchResponse
import com.example.androidplayistcreator.serivce.YTDLPApiService
import com.example.androidplayistcreator.views.recycler_view_adapters.SearchResultRvAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultRvAdapter
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private val apiService = YTDLPApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultRvAdapter(emptyList()) { track ->
            val resultIntent = Intent()
            resultIntent.putExtra("SELECTED_TRACK", track.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                showAlert("Search failed", "Please enter a search query.")
            }
        }
    }

    private fun performSearch(query: String) {
        apiService.searchVideos(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.results?.map {
                        val videoId = it.url.split("v=").getOrNull(1)?.split("&")?.getOrNull(0) ?: ""
                        Track(
                            artist = it.artist,
                            name = it.title,
                            step = 1,  // Adjust as needed
                            video_id = videoId,
                            duration = it.duration,
                            isSubTrack = false,
                            source = "YOUTUBE"
                        )
                    } ?: emptyList()
                    adapter.updateTracks(searchResults)
                } else {
                    showAlert("Search failed", "Unable to fetch search results.")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e("SearchActivity", "Search failed", t)
                showAlert("Search failed", "Unable to fetch search results.")
            }
        })
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}