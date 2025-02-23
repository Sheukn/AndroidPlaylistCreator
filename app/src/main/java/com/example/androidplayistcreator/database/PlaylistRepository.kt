import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.androidplayistcreator.models.Playlist

class PlaylistRepository(context: Context) {
    private val dbHelper = PlaylistDatabaseHelper(context)

    fun addPlaylist(playlist: Playlist): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlaylistDatabaseHelper.COLUMN_NAME, playlist.name)
            put(PlaylistDatabaseHelper.COLUMN_IMAGE_URL, playlist.imageUrl)
        }
        return db.insert(PlaylistDatabaseHelper.TABLE_PLAYLIST, null, values)
    }

    fun getAllPlaylists(): List<Playlist> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            PlaylistDatabaseHelper.TABLE_PLAYLIST,
            null, null, null, null, null, null
        )
        val playlists = mutableListOf<Playlist>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(PlaylistDatabaseHelper.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(PlaylistDatabaseHelper.COLUMN_NAME))
                val imageUrl = getString(getColumnIndexOrThrow(PlaylistDatabaseHelper.COLUMN_IMAGE_URL))
                playlists.add(Playlist(emptyList(), id, name, emptyList(), imageUrl))
            }
        }
        cursor.close()
        return playlists
    }

    fun deletePlaylist(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(PlaylistDatabaseHelper.TABLE_PLAYLIST, "${PlaylistDatabaseHelper.COLUMN_ID}=?", arrayOf(id.toString()))
    }
}