package com.vibetra.app.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.vibetra.app.data.db.entity.PlaylistEntity;
import com.vibetra.app.data.db.entity.PlaylistTrackEntity;
import com.vibetra.app.data.model.PlaylistSummary;
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
public final class PlaylistDao_Impl implements PlaylistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaylistEntity> __insertionAdapterOfPlaylistEntity;

  private final EntityInsertionAdapter<PlaylistTrackEntity> __insertionAdapterOfPlaylistTrackEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeletePlaylist;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTracksByPlaylistId;

  private final SharedSQLiteStatement __preparedStmtOfRemoveTrackFromPlaylist;

  public PlaylistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaylistEntity = new EntityInsertionAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playlists` (`id`,`name`,`createdAt`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PlaylistEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfPlaylistTrackEntity = new EntityInsertionAdapter<PlaylistTrackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playlist_tracks` (`playlistId`,`trackKey`,`trackId`,`source`,`title`,`artistName`,`artworkUrl`,`streamUrl`,`durationSec`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PlaylistTrackEntity entity) {
        statement.bindLong(1, entity.getPlaylistId());
        if (entity.getTrackKey() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTrackKey());
        }
        if (entity.getTrackId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTrackId());
        }
        if (entity.getSource() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSource());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitle());
        }
        if (entity.getArtistName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getArtistName());
        }
        if (entity.getArtworkUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getArtworkUrl());
        }
        if (entity.getStreamUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getStreamUrl());
        }
        statement.bindLong(9, entity.getDurationSec());
      }
    };
    this.__preparedStmtOfDeletePlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlists WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTracksByPlaylistId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_tracks WHERE playlistId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveTrackFromPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_tracks WHERE playlistId = ? AND trackKey = ?";
        return _query;
      }
    };
  }

  @Override
  public long insertPlaylist(final PlaylistEntity playlist) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfPlaylistEntity.insertAndReturnId(playlist);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void addTrackToPlaylist(final PlaylistTrackEntity track) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPlaylistTrackEntity.insert(track);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletePlaylist(final long playlistId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePlaylist.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, playlistId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletePlaylist.release(_stmt);
    }
  }

  @Override
  public void deleteTracksByPlaylistId(final long playlistId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTracksByPlaylistId.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, playlistId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteTracksByPlaylistId.release(_stmt);
    }
  }

  @Override
  public void removeTrackFromPlaylist(final long playlistId, final String trackKey) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveTrackFromPlaylist.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, playlistId);
    _argIndex = 2;
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
      __preparedStmtOfRemoveTrackFromPlaylist.release(_stmt);
    }
  }

  @Override
  public LiveData<List<PlaylistEntity>> getPlaylists() {
    final String _sql = "SELECT * FROM playlists ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"playlists"}, false, new Callable<List<PlaylistEntity>>() {
      @Override
      @Nullable
      public List<PlaylistEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<PlaylistEntity> _result = new ArrayList<PlaylistEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistEntity _item;
            _item = new PlaylistEntity();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            _item.setName(_tmpName);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
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
  public LiveData<List<PlaylistSummary>> getPlaylistSummaries() {
    final String _sql = "SELECT p.id AS id, p.name AS name, p.createdAt AS createdAt, (SELECT COUNT(*) FROM playlist_tracks pt WHERE pt.playlistId = p.id) AS trackCount, (SELECT pt2.artworkUrl FROM playlist_tracks pt2 WHERE pt2.playlistId = p.id AND pt2.artworkUrl IS NOT NULL AND pt2.artworkUrl != '' LIMIT 1) AS firstArtworkUrl FROM playlists p ORDER BY p.createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"playlist_tracks",
        "playlists"}, false, new Callable<List<PlaylistSummary>>() {
      @Override
      @Nullable
      public List<PlaylistSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfName = 1;
          final int _cursorIndexOfCreatedAt = 2;
          final int _cursorIndexOfTrackCount = 3;
          final int _cursorIndexOfFirstArtworkUrl = 4;
          final List<PlaylistSummary> _result = new ArrayList<PlaylistSummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistSummary _item;
            _item = new PlaylistSummary();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            _item.setName(_tmpName);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final int _tmpTrackCount;
            _tmpTrackCount = _cursor.getInt(_cursorIndexOfTrackCount);
            _item.setTrackCount(_tmpTrackCount);
            final String _tmpFirstArtworkUrl;
            if (_cursor.isNull(_cursorIndexOfFirstArtworkUrl)) {
              _tmpFirstArtworkUrl = null;
            } else {
              _tmpFirstArtworkUrl = _cursor.getString(_cursorIndexOfFirstArtworkUrl);
            }
            _item.setFirstArtworkUrl(_tmpFirstArtworkUrl);
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
  public List<PlaylistEntity> getPlaylistsNow() {
    final String _sql = "SELECT * FROM playlists ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final List<PlaylistEntity> _result = new ArrayList<PlaylistEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PlaylistEntity _item;
        _item = new PlaylistEntity();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<PlaylistTrackEntity>> getTracksForPlaylist(final long playlistId) {
    final String _sql = "SELECT * FROM playlist_tracks WHERE playlistId = ? ORDER BY title COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"playlist_tracks"}, false, new Callable<List<PlaylistTrackEntity>>() {
      @Override
      @Nullable
      public List<PlaylistTrackEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlaylistId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistId");
          final int _cursorIndexOfTrackKey = CursorUtil.getColumnIndexOrThrow(_cursor, "trackKey");
          final int _cursorIndexOfTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackId");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtistName = CursorUtil.getColumnIndexOrThrow(_cursor, "artistName");
          final int _cursorIndexOfArtworkUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "artworkUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final List<PlaylistTrackEntity> _result = new ArrayList<PlaylistTrackEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistTrackEntity _item;
            _item = new PlaylistTrackEntity();
            final long _tmpPlaylistId;
            _tmpPlaylistId = _cursor.getLong(_cursorIndexOfPlaylistId);
            _item.setPlaylistId(_tmpPlaylistId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
