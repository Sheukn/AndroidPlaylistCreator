package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Playlist

class PlaylistsRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val playlistImage: ImageView = itemView.findViewById(R.id.playlist_id)
    val playlistTitle: TextView = itemView.findViewById(R.id.playlist_title)
    val playlistDetailsRecyclerView: RecyclerView = itemView.findViewById(R.id.playlist_details_recyclerView)

    // You can add methods to bind data if needed
    fun bind(playlist: Playlist) {
        playlistTitle.text = playlist.name
        Glide.with(itemView.context)
            .load(playlist.url)
            .error(R.mipmap.ic_launcher_round) // Optional error placeholder
            .into(playlistImage)
    }
}
