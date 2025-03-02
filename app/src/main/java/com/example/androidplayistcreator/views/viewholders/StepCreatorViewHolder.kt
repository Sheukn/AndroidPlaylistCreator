package com.example.androidplayistcreator.views.viewholders

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.activities.SearchActivity
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.singletons.PlaylistSingleton
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListCreatorRvAdapter
import com.example.androidplayistcreator.views.recycler_view_adapters.SubTrackListRvAdapter
import com.google.android.material.card.MaterialCardView

class StepCreatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTextView: TextView = itemView.findViewById(R.id.trackTextView)
    private val trackImageView: ImageView = itemView.findViewById(R.id.trackImageView)
    private val subTrackCardView: MaterialCardView = itemView.findViewById(R.id.subTrackListCardView)
    private val subTracksRecyclerView: RecyclerView = itemView.findViewById(R.id.secondaryTrackListRecyclerView)
    private val mainTrackCardView = itemView.findViewById<MaterialCardView>(R.id.mainTrackCardView)
    private val addTrackTextView = itemView.findViewById<TextView>(R.id.addTrackTextView)

    fun bind(step: Step, position: Int) {
        trackTextView.text = step.mainTrack.name
        if (step.mainTrack.artwork != null) {
            Glide.with(itemView.context)
                .load(step.mainTrack.artwork)
                .into(trackImageView)
        } else {
            trackImageView.setImageResource(R.drawable.audius)
        }

        if (step.subTracks.isNotEmpty()) {
            subTrackCardView.visibility = View.VISIBLE // Show sub-track card
            val adapter = SubTrackListCreatorRvAdapter(step.subTracks)
            subTracksRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            subTracksRecyclerView.adapter = adapter
        } else {
            subTrackCardView.visibility = View.GONE // Hide sub-track card if no sub-tracks
        }
        mainTrackCardView.setOnClickListener {
            // Store the current step position in the singleton
            PlaylistSingleton.currentStepPosition = position
            val intent = Intent(itemView.context, SearchActivity::class.java)
            intent.putExtra("isMainTrack", true)
            (itemView.context as Activity).startActivityForResult(intent, 1)
        }

        addTrackTextView.setOnClickListener {
            // Store the current step position in the singleton
            PlaylistSingleton.currentStepPosition = position
            val intent = Intent(itemView.context, SearchActivity::class.java)
            intent.putExtra("isMainTrack", false)
            (itemView.context as Activity).startActivityForResult(intent, 1)
        }
    }
}