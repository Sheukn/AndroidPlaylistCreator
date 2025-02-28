package com.example.androidplayistcreator.views.recycler_view_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.relations.StepWithTracks
import com.example.androidplayistcreator.views.viewholders.StepViewHolder

class StepsRvAdapter(private var steps: List<StepWithTracks>) : RecyclerView.Adapter<StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newSteps: List<StepWithTracks>) {
        steps = newSteps
        for (step in steps) {
            for (track in step.tracks) {
                println("com.example.androidplayistcreator.models.Track: ${track.name}")
            }
        }
        notifyDataSetChanged()
    }
}