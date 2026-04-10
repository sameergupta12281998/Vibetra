package com.vibetra.app.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vibetra.app.data.db.entity.FavoriteTrackEntity;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteTrackEntity entity);

    @Delete
    void delete(FavoriteTrackEntity entity);

    @Query("DELETE FROM favorite_tracks WHERE trackKey = :trackKey")
    void deleteByTrackKey(String trackKey);

    @Query("SELECT * FROM favorite_tracks ORDER BY title COLLATE NOCASE ASC")
    LiveData<List<FavoriteTrackEntity>> getAll();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE trackKey = :trackKey)")
    boolean isFavorite(String trackKey);
}
