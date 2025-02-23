package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

    fun bind(track: Track) {
        artistTextView.text = track.artist
        nameTextView.text = track.name
    }
}