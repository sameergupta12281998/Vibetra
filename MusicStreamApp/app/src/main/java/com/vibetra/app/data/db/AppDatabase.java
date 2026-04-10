package com.vibetra.app.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vibetra.app.data.db.dao.FavoritesDao;
import com.vibetra.app.data.db.dao.PlaylistDao;
import com.vibetra.app.data.db.entity.FavoriteTrackEntity;
import com.vibetra.app.data.db.entity.PlaylistEntity;
import com.vibetra.app.data.db.entity.PlaylistTrackEntity;

@Database(
        entities = {FavoriteTrackEntity.class, PlaylistEntity.class, PlaylistTrackEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract FavoritesDao favoritesDao();

    public abstract PlaylistDao playlistDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "music_stream.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
