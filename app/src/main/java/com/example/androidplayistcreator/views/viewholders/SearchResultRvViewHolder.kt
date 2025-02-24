package com.example.androidplayistcreator.views.viewholders

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplayistcreator.R
import com.example.androidplayistcreator.models.Track

class SearchResultRvViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
    val searchResultTitle: TextView = itemView.findViewById(R.id.resultTextView)
    val searchResultSource: ImageView = itemView.findViewById(R.id.sourceImageView)
    fun bind(searchResult: Track){
        searchResultTitle.text = searchResult.name
        searchResultTitle.isSelected = true

        if (searchResult.source == "YouTube") {
            searchResultSource.setImageResource(R.drawable.youtube)
        } else {
            searchResultSource.setImageResource(R.drawable.audius)
        }

        searchResultTitle.post {
            if (searchResultTitle.layout != null && searchResultTitle.width < searchResultTitle.paint.measureText(
                    searchResult.name
                )
            ) {
                startScrollingAnimation(searchResultTitle, searchResultTitle.width)
            }
        }
    }

    private fun startScrollingAnimation(textView: TextView, containerWidth: Int) {
        val textWidth = textView.paint.measureText(textView.text.toString())

        if (textWidth > containerWidth) {
            ObjectAnimator.ofInt(textView, "scrollX", (textWidth - containerWidth).toInt()).apply {
                duration = 4000L
                interpolator = LinearInterpolator()
                doOnEnd {
                    textView.scrollX = 0 // Réinitialisation après un cycle
                }
                start()
            }
        }
    }

}
