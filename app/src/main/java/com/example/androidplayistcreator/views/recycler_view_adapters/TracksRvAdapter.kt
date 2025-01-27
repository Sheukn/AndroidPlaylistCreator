package com.example.androidplayistcreator.views.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Step
import com.example.androidplayistcreator.views.viewholders.mainTrackRvViewHolder
import com.example.androidplayistcreator.views.viewholders.secondaryTrackListRvViewHolder

class TracksRvAdapter(val steps: List<Step>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val MAIN_TRACK_TYPE = 0
        private const val SUB_TRACK_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        // Get the step and the position within that step
        val stepIndex = getStepIndex(position)
        val step = steps[stepIndex]
        val trackPositionInStep = position - getStartIndexForStep(stepIndex)

        return if (trackPositionInStep == 0) MAIN_TRACK_TYPE else SUB_TRACK_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MAIN_TRACK_TYPE -> {
                val view = inflater.inflate(R.layout.main_track_view_holder, parent, false)
                mainTrackRvViewHolder(view)
            }

            SUB_TRACK_TYPE -> {
                val view =
                    inflater.inflate(R.layout.secondary_track_list_view_holder, parent, false)
                secondaryTrackListRvViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val stepIndex = getStepIndex(position)
        val step = steps[stepIndex]
        val trackPositionInStep = position - getStartIndexForStep(stepIndex)

        if (trackPositionInStep == 0) {
            // Bind the main track
            (holder as mainTrackRvViewHolder).bind(step.mainTrack)
        } else {
            // Bind the sub-track
            (holder as secondaryTrackListRvViewHolder).bind(step.subTracks)
        }
    }

    override fun getItemCount(): Int {
        // Calculate the total item count: main track + sub-tracks for each step
        return steps.sumBy { it.subTracks.size + 1 }
    }

    private fun getStartIndexForStep(stepIndex: Int): Int {
        return (0 until stepIndex).sumOf { steps[it].subTracks.size + 1 }
    }

    private fun getStepIndex(position: Int): Int {
        var currentIndex = 0
        var cumulativeCount = 0

        while (currentIndex < steps.size) {
            val step = steps[currentIndex]
            cumulativeCount += step.subTracks.size + 1
            if (position < cumulativeCount) {
                return currentIndex
            }
            currentIndex++
        }
        throw IndexOutOfBoundsException("Invalid position: $position")
    }
}
