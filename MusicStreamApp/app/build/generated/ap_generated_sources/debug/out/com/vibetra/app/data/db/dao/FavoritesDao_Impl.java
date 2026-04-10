package com.vibetra.app.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vibetra.app.data.db.entity.FavoriteTrackEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FavoritesDao_Impl implements FavoritesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FavoriteTrackEntity> __insertionAdapterOfFavoriteTrackEntity;

  private final EntityDeletionOrUpdateAdapter<FavoriteTrackEntity> __deletionAdapterOfFavoriteTrackEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByTrackKey;

  public FavoritesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavoriteTrackEntity = new EntityInsertionAdapter<FavoriteTrackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `favorite_tracks` (`trackKey`,`trackId`,`source`,`title`,`artistName`,`artworkUrl`,`streamUrl`,`durationSec`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FavoriteTrackEntity entity) {
        if (entity.getTrackKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getTrackKey());
        }
        if (entity.getTrackId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTrackId());
        }
        if (entity.getSource() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSource());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        if (entity.getArtistName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getArtistName());
        }
        if (entity.getArtworkUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getArtworkUrl());
        }
        if (entity.getStreamUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getStreamUrl());
        }
        statement.bindLong(8, entity.getDurationSec());
      }
    };
    this.__deletionAdapterOfFavoriteTrackEntity = new EntityDeletionOrUpdateAdapter<FavoriteTrackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `favorite_tracks` WHERE `trackKey` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FavoriteTrackEntity entity) {
        if (entity.getTrackKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getTrackKey());
        }
      }
    };
    this.__preparedStmtOfDeleteByTrackKey = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM favorite_tracks WHERE trackKey = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final FavoriteTrackEntity entity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFavoriteTrackEntity.insert(entity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final FavoriteTrackEntity entity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFavoriteTrackEntity.handle(entity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByTrackKey(final String trackKey) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByTrackKey.acquire();
    int _argIndex = 1;
    if (trackKey == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, trackKey);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteByTrackKey.release(_stmt);
    }
  }

  @Override
  public LiveData<List<FavoriteTrackEntity>> getAll() {
    final String _sql = "SELECT * FROM favorite_tracks ORDER BY title COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"favorite_tracks"}, false, new Callable<List<FavoriteTrackEntity>>() {
      @Override
      @Nullable
      public List<FavoriteTrackEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTrackKey = CursorUtil.getColumnIndexOrThrow(_cursor, "trackKey");
          final int _cursorIndexOfTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackId");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtistName = CursorUtil.getColumnIndexOrThrow(_cursor, "artistName");
          final int _cursorIndexOfArtworkUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "artworkUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final List<FavoriteTrackEntity> _result = new ArrayList<FavoriteTrackEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavoriteTrackEntity _item;
            _item = new FavoriteTrackEntity();
            final String _tmpTrackKey;
            if (_cursor.isNull(_cursorIndexOfTrackKey)) {
              _tmpTrackKey = null;
            } else {
              _tmpTrackKey = _cursor.getString(_cursorIndexOfTrackKey);
            }
            _item.setTrackKey(_tmpTrackKey);
            final String _tmpTrackId;
            if (_cursor.isNull(_cursorIndexOfTrackId)) {
              _tmpTrackId = null;
            } else {
              _tmpTrackId = _cursor.getString(_cursorIndexOfTrackId);
            }
            _item.setTrackId(_tmpTrackId);
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            _item.setSource(_tmpSource);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpArtistName;
            if (_cursor.isNull(_cursorIndexOfArtistName)) {
              _tmpArtistName = null;
            } else {
              _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
            }
            _item.setArtistName(_tmpArtistName);
            final String _tmpArtworkUrl;
            if (_cursor.isNull(_cursorIndexOfArtworkUrl)) {
              _tmpArtworkUrl = null;
            } else {
              _tmpArtworkUrl = _cursor.getString(_cursorIndexOfArtworkUrl);
            }
            _item.setArtworkUrl(_tmpArtworkUrl);
            final String _tmpStreamUrl;
            if (_cursor.isNull(_cursorIndexOfStreamUrl)) {
              _tmpStreamUrl = null;
            } else {
              _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            }
            _item.setStreamUrl(_tmpStreamUrl);
            final long _tmpDurationSec;
            _tmpDurationSec = _cursor.getLong(_cursorIndexOfDurationSec);
            _item.setDurationSec(_tmpDurationSec);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public boolean isFavorite(final String trackKey) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE trackKey = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (trackKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, trackKey);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final boolean _result;
      if (_cursor.moveToFirst()) {
        final int _tmp;
        _tmp = _cursor.getInt(0);
        _result = _tmp != 0;
      } else {
        _result = false;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
