package com.example.androidplayistcreator.models.singletons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Looper
import com.example.androidplayistcreator.database.entities.TrackEntity

object TrackSingleton {
    private val _currentTrack = MutableLiveData<TrackEntity?>()
    val currentTrack: LiveData<TrackEntity?> = _currentTrack  // Exposed LiveData

    private val mainHandler = Handler(Looper.getMainLooper())  // Ensure main thread execution

    fun getCurrentTrackId(): Int {
        return _currentTrack.value?.id ?: 0
    }

    fun getCurrentTrack(): TrackEntity? {
        return _currentTrack.value
    }

    fun setCurrentTrack(track: TrackEntity) {
        mainHandler.post {  // Ensure update happens on main thread
            _currentTrack.value = track
        }
    }

    fun clearTrack() {
        mainHandler.post {  // Ensure update happens on main thread
            _currentTrack.value = null
        }
    }
}

