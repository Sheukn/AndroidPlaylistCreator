package com.example.androidplayistcreator.views.recycler_view_adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.views.viewholders.StepCreatorViewHolder

class StepCreatorRvAdapter(private var steps: List<Step>) :
    RecyclerView.Adapter<StepCreatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepCreatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_step, parent, false)
        return StepCreatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepCreatorViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newSteps: List<Step>) {
        steps = newSteps
        for (step in steps) {
            for (track in steps) {
                Log.d("StepsRvAdapter", "com.example.androidplayistcreator.models.Track: ${track.mainTrack.name}")
            }
        }
        notifyDataSetChanged()
    }
}