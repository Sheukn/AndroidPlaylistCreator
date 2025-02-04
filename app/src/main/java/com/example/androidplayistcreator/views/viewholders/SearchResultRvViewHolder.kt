package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track

class SearchResultRvViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
    val searchResultTitle: TextView = itemView.findViewById(R.id.resultTextView)
    val searchResultSource: ImageView = itemView.findViewById(R.id.sourceImageView)
    fun bind(searchResult: Track){
        searchResultTitle.text = searchResult.name
        if (searchResult.source == "SPOTIFY") {
            searchResultSource.setImageResource(R.drawable.spotify)
        } else {
            searchResultSource.setImageResource(R.drawable.youtube)
        }
    }
}