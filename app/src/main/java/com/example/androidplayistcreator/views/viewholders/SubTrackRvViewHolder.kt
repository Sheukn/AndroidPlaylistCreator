package com.example.androidplayistcreator.views.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.database.entities.TrackEntity

class SubTrackRvViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
    val dotView: View = itemView.findViewById(R.id.dotView)
    val trackTitle: TextView = itemView.findViewById(R.id.subTrackTextView)

    fun bind(track: TrackEntity){
        trackTitle.text = track.name

        trackTitle.post {
            if (trackTitle.layout != null && trackTitle.width < trackTitle.paint.measureText(track.name)) {
                trackTitle.isSelected = true  // Ensures marquee effect starts
            }
        }
        val random = randomMethod()
        when(random){
            1 -> dotView.setBackgroundResource(R.drawable.blue_circle_dot)
            2 -> dotView.setBackgroundResource(R.drawable.green_circle_dot)
            3 -> dotView.setBackgroundResource(R.drawable.red_circle_dot)
        }
    }

    fun randomMethod() : Int{
        // return 1 to 3
        return (1..3).random()
    }
}