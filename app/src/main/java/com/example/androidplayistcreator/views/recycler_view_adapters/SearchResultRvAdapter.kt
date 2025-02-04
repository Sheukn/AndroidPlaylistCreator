package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.SearchResultRvViewHolder

class SearchResultRvAdapter(val searchResults: List<Track>):
    RecyclerView.Adapter<SearchResultRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_search_results, parent, false)
        return SearchResultRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultRvViewHolder, position: Int) {
        val searchResult = searchResults[position]
        holder.bind(searchResult)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }
}