package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.subTrackRvViewHolder

class SubTrackListRvAdapter(val subTracks: List<Track>):
    RecyclerView.Adapter<subTrackRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subTrackRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_track_view_holder, parent, false)
        return subTrackRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: subTrackRvViewHolder, position: Int) {
        val track = subTracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return subTracks.size
    }
}