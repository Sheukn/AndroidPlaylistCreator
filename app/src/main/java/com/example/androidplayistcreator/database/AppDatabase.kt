package com.example.androidplayistcreator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidplayistcreator.database.dao.PlaylistDao
import com.example.androidplayistcreator.database.entities.*
import com.example.androidplayistcreator.database.relations.*

@Database(entities = [PlaylistEntity::class, StepEntity::class, TrackEntity::class], views = [StepWithTracks::class, PlaylistWithSteps::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playlist_database"
                )
                    .fallbackToDestructiveMigration() // 🔥 WARNING: This will wipe existing data!
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}