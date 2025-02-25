package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.entities.PlaylistEntity
import com.example.androidplayistcreator.views.viewholders.PlaylistsRvViewHolder

class PlaylistsRvAdapter(val playlists: List<PlaylistEntity>):
    RecyclerView.Adapter<PlaylistsRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_playlist, parent, false)
        return PlaylistsRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsRvViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}