package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Playlist

class mainTrackRvViewHolders (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackImage: ImageView = itemView.findViewById(R.id.trackImageView)
    val trackTitle: TextView = itemView.findViewById(R.id.playlist_title)
    val trackDuration: TextView = itemView.findViewById(R.id.trackDurationTextView)
}