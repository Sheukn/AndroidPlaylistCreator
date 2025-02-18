package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter
import com.google.android.material.card.MaterialCardView

class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTextView: TextView = itemView.findViewById(R.id.trackTextView)
    private val trackDurationTextView: TextView = itemView.findViewById(R.id.trackDurationTextView)
    private val trackImageView: ImageView = itemView.findViewById(R.id.trackImageView)

    private val subTrackCardView: MaterialCardView = itemView.findViewById(R.id.subTrackListCardView)
    private val subTracksRecyclerView: RecyclerView = itemView.findViewById(R.id.secondaryTrackListRecyclerView)

    fun bind(step: Step) {
        trackTextView.text = step.mainTrack.name
        trackDurationTextView.text = "Duration: ${step.mainTrack.duration}"
        trackImageView.setImageResource(R.mipmap.ic_launcher) // Replace with actual image

        if (step.subTracks.isNotEmpty()) {
            subTrackCardView.visibility = View.VISIBLE // Show sub-track card
            val adapter = SubTrackListRvAdapter(step.subTracks)
            subTracksRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            subTracksRecyclerView.adapter = adapter
        } else {
            subTrackCardView.visibility = View.GONE // Hide sub-track card if no sub-tracks
        }
    }
}

