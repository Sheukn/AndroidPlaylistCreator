package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.views.viewholders.mainTrackRvViewHolder
import com.example.androidplayistcreator.views.viewholders.secondaryTrackListRvViewHolder
import com.example.androidplayistcreator.views.viewholders.subTrackRvViewHolder

class TracksRvAdapter(private val steps: List<Step>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MAIN_TRACK = 0
        private const val TYPE_SUB_TRACK_LIST = 1
        private const val TYPE_SUB_TRACK = 2
    }

    override fun getItemViewType(position: Int): Int {
        // On vérifie si l'élément est un mainTrack ou une liste de subTracks
        val step = steps[position]
        return when {
            position % 2 == 0 -> TYPE_MAIN_TRACK
            step.subTracks.isNotEmpty() -> TYPE_SUB_TRACK_LIST
            else -> TYPE_SUB_TRACK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MAIN_TRACK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_main_track, parent, false)
                mainTrackRvViewHolder(view)
            }
            TYPE_SUB_TRACK_LIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_subtracks_list, parent, false)
                secondaryTrackListRvViewHolder(view)
            }
            TYPE_SUB_TRACK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_subtrack, parent, false)
                subTrackRvViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val step = steps[position]
        when (holder) {
            is mainTrackRvViewHolder -> holder.bind(step.mainTrack)
            is secondaryTrackListRvViewHolder -> holder.bind(step.subTracks)
            is subTrackRvViewHolder -> holder.bind(step.subTracks[position])
        }
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}

