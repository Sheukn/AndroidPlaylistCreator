package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.CreatePlaylistStepRvViewHolder

class CreatePlaylistStepRVAdapter (val subTracks : List<Track>):
    RecyclerView.Adapter<CreatePlaylistStepRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatePlaylistStepRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_create_playlist_step, parent, false)
        return CreatePlaylistStepRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreatePlaylistStepRvViewHolder, position: Int) {
        val track = subTracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return subTracks.size
    }
}