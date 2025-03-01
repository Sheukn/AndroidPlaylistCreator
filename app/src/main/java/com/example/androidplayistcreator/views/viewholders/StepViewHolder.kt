package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.relations.StepWithTracks
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter
import com.google.android.material.card.MaterialCardView

class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTextView: TextView = itemView.findViewById(R.id.trackTextView)
    private val trackDurationTextView: TextView = itemView.findViewById(R.id.trackDurationTextView)
    private val trackImageView: ImageView = itemView.findViewById(R.id.trackImageView)
    private val subTrackCardView: MaterialCardView = itemView.findViewById(R.id.subTrackListCardView)
    private val subTracksRecyclerView: RecyclerView = itemView.findViewById(R.id.secondaryTrackListRecyclerView)

    fun bind(step: StepWithTracks) {
        val tracks = step.tracks
        val mainTrack = tracks.find { !it.isSubTrack }
        val subTracks = tracks.filter { it.isSubTrack }
        // Set main track data
        trackTextView.text = mainTrack?.name
        trackDurationTextView.text = mainTrack?.duration

        trackTextView.post {
            if (trackTextView.layout != null && trackTextView.width < trackTextView.paint.measureText(mainTrack?.name ?: "")) {
                trackTextView.isSelected = true  // Ensures marquee effect starts
            }
        }

        Glide.with(itemView.context).load(mainTrack?.artwork).into(trackImageView)

        if (subTracks.isNotEmpty()) {
            subTrackCardView.visibility = View.VISIBLE // Show sub-track card
            val adapter = SubTrackListRvAdapter(subTracks)
            subTracksRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            subTracksRecyclerView.adapter = adapter
        } else {
            subTrackCardView.visibility = View.GONE // Hide sub-track card if no sub-tracks
        }
    }
}

