package com.example.androidplayistcreator.views.recycler_view_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.models.Track
import com.example.androidplayistcreator.views.viewholders.CreatePlaylistStepRvViewHolder
import com.example.androidplayistcreator.views.viewholders.StepCreatorViewHolder

class CreatePlaylistStepRVAdapter(private var steps: List<Step>) : RecyclerView.Adapter<StepCreatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepCreatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_create_playlist_step, parent, false)
        return StepCreatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepCreatorViewHolder, position: Int) {
        holder.bind(steps[position], position) // Pass the position
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    // Update the list and notify the adapter
    @SuppressLint("NotifyDataSetChanged")
    fun updateSteps(newSteps: List<Step>) {
        steps = newSteps
        notifyDataSetChanged()
    }
}
