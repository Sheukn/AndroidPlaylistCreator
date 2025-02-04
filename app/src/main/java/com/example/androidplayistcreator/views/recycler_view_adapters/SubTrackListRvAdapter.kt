package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.SubTrackRvViewHolder

class SubTrackListRvAdapter(val subTracks: List<Track>):
    RecyclerView.Adapter<SubTrackRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTrackRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_subtrack, parent, false)
        return SubTrackRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTrackRvViewHolder, position: Int) {
        val track = subTracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return subTracks.size
    }
}