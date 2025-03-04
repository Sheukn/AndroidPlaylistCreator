package com.example.androidplayistcreator.views.recycler_view_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.SubTrackRvViewHolder

class SubTrackListCreatorRvAdapter(private val subTracks: MutableList<Track>) : RecyclerView.Adapter<SubTrackRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTrackRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_subtrack, parent, false)
        return SubTrackRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTrackRvViewHolder, position: Int) {
        holder.bind(subTracks[position])
    }

    override fun getItemCount(): Int = subTracks.size

    // Add a method to update the dataset
    @SuppressLint("NotifyDataSetChanged")
    fun updateSubTracks(newSubTracks: List<Track>) {
        subTracks.clear()
        subTracks.addAll(newSubTracks)
        notifyDataSetChanged()
    }
}
