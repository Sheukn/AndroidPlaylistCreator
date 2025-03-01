package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter
import com.google.android.material.card.MaterialCardView

class StepCreatorViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTextView: TextView = itemView.findViewById(R.id.trackTextView)
    private val trackImageView: ImageView = itemView.findViewById(R.id.trackImageView)
    private val subTrackCardView: MaterialCardView = itemView.findViewById(R.id.subTrackListCardView)
    private val subTracksRecyclerView: RecyclerView = itemView.findViewById(R.id.secondaryTrackListRecyclerView)
    private val mainTrackCardView = itemView.findViewById<MaterialCardView>(R.id.mainTrackCardView)

    fun bind(step: Step) {
        if (step.mainTrack == null) {
            trackTextView.text = "No track"
        } else
            trackTextView.text = step.mainTrack.name

    }
}