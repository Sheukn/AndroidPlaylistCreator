package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter
class mainTrackRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImageView)
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTextView)
    private val trackDuration: TextView = itemView.findViewById(R.id.trackDurationTextView)

    fun bind(track: Track) {
        trackTitle.text = track.name
        // Ajouter des données de track ici si nécessaire
    }
}
