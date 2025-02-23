package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.SearchResultRvViewHolder

class SearchResultRvAdapter(
    private var searchResults: List<Track>,
    private val onTrackSelected: (Track) -> Unit
) : RecyclerView.Adapter<SearchResultRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_search_results, parent, false)
        return SearchResultRvViewHolder(view)
    }

    fun updateTracks(newSearchResults: List<Track>) {
        searchResults = newSearchResults
    }

    override fun onBindViewHolder(holder: SearchResultRvViewHolder, position: Int) {
        val searchResult = searchResults[position]
        holder.bind(searchResult)
        holder.itemView.setOnClickListener {
            onTrackSelected(searchResult)
        }
    }

    override fun getItemCount(): Int = searchResults.size
}