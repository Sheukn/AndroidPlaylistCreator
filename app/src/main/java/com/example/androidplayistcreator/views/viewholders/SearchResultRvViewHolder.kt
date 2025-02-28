package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track

class SearchResultRvViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
    val searchResultTitle: TextView = itemView.findViewById(R.id.resultTextView)
    val searchResultCover: ImageView = itemView.findViewById(R.id.sourceImageView)
    fun bind(searchResult: Track){
        searchResultTitle.text = searchResult.name
        searchResultTitle.isSelected = true
        if (searchResult.artwork.equals("No artwork")){
            searchResultCover.setImageResource(R.drawable.audius)
        } else {
            Glide.with(itemView.context).load(searchResult.artwork).into(searchResultCover)
        }

        searchResultTitle.post {
            if (searchResultTitle.layout != null && searchResultTitle.width < searchResultTitle.paint.measureText(
                    searchResult?.name ?: ""
                )
            ) {
                searchResultTitle.isSelected = true
            }
        }
    }
}
