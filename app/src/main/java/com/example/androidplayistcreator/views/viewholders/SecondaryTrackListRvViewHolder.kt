package com.example.androidplayistcreator.views.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.entities.TrackEntity
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter

class SecondaryTrackListRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val secondaryTrackListRecyclerView: RecyclerView = itemView.findViewById(R.id.secondaryTrackListRecyclerView)

    fun bind(subTracks: List<TrackEntity>) {
        val adapter = SubTrackListRvAdapter(subTracks)
        secondaryTrackListRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        secondaryTrackListRecyclerView.adapter = adapter
    }
}

