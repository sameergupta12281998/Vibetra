package com.vibetra.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.vibetra.app.data.db.dao.FavoritesDao;
import com.vibetra.app.data.db.dao.FavoritesDao_Impl;
import com.vibetra.app.data.db.dao.PlaylistDao;
import com.vibetra.app.data.db.dao.PlaylistDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FavoritesDao _favoritesDao;

  private volatile PlaylistDao _playlistDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_tracks` (`trackKey` TEXT NOT NULL, `trackId` TEXT, `source` TEXT, `title` TEXT, `artistName` TEXT, `artworkUrl` TEXT, `streamUrl` TEXT, `durationSec` INTEGER NOT NULL, PRIMARY KEY(`trackKey`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_tracks` (`playlistId` INTEGER NOT NULL, `trackKey` TEXT NOT NULL, `trackId` TEXT, `source` TEXT, `title` TEXT, `artistName` TEXT, `artworkUrl` TEXT, `streamUrl` TEXT, `durationSec` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `trackKey`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_tracks_playlistId` ON `playlist_tracks` (`playlistId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ef2d2e3ced4f03d6ae34af7a5cd7a186')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorite_tracks`");
        db.execSQL("DROP TABLE IF EXISTS `playlists`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_tracks`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFavoriteTracks = new HashMap<String, TableInfo.Column>(8);
        _columnsFavoriteTracks.put("trackKey", new TableInfo.Column("trackKey", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("trackId", new TableInfo.Column("trackId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("artistName", new TableInfo.Column("artistName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("artworkUrl", new TableInfo.Column("artworkUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("streamUrl", new TableInfo.Column("streamUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTracks.put("durationSec", new TableInfo.Column("durationSec", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteTracks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteTracks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteTracks = new TableInfo("favorite_tracks", _columnsFavoriteTracks, _foreignKeysFavoriteTracks, _indicesFavoriteTracks);
        final TableInfo _existingFavoriteTracks = TableInfo.read(db, "favorite_tracks");
        if (!_infoFavoriteTracks.equals(_existingFavoriteTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_tracks(com.vibetra.app.data.db.entity.FavoriteTrackEntity).\n"
                  + " Expected:\n" + _infoFavoriteTracks + "\n"
                  + " Found:\n" + _existingFavoriteTracks);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylists = new HashMap<String, TableInfo.Column>(3);
        _columnsPlaylists.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylists.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylists = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylists = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylists = new TableInfo("playlists", _columnsPlaylists, _foreignKeysPlaylists, _indicesPlaylists);
        final TableInfo _existingPlaylists = TableInfo.read(db, "playlists");
        if (!_infoPlaylists.equals(_existingPlaylists)) {
          return new RoomOpenHelper.ValidationResult(false, "playlists(com.vibetra.app.data.db.entity.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylists + "\n"
                  + " Found:\n" + _existingPlaylists);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylistTracks = new HashMap<String, TableInfo.Column>(9);
        _columnsPlaylistTracks.put("playlistId", new TableInfo.Column("playlistId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("trackKey", new TableInfo.Column("trackKey", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("trackId", new TableInfo.Column("trackId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("artistName", new TableInfo.Column("artistName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("artworkUrl", new TableInfo.Column("artworkUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("streamUrl", new TableInfo.Column("streamUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTracks.put("durationSec", new TableInfo.Column("durationSec", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistTracks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylistTracks = new HashSet<TableInfo.Index>(1);
        _indicesPlaylistTracks.add(new TableInfo.Index("index_playlist_tracks_playlistId", false, Arrays.asList("playlistId"), Arrays.asList("ASC")));
        final TableInfo _infoPlaylistTracks = new TableInfo("playlist_tracks", _columnsPlaylistTracks, _foreignKeysPlaylistTracks, _indicesPlaylistTracks);
        final TableInfo _existingPlaylistTracks = TableInfo.read(db, "playlist_tracks");
        if (!_infoPlaylistTracks.equals(_existingPlaylistTracks)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_tracks(com.vibetra.app.data.db.entity.PlaylistTrackEntity).\n"
                  + " Expected:\n" + _infoPlaylistTracks + "\n"
                  + " Found:\n" + _existingPlaylistTracks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ef2d2e3ced4f03d6ae34af7a5cd7a186", "c489a2d42d2a223db2d7218fd2664135");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_tracks","playlists","playlist_tracks");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorite_tracks`");
      _db.execSQL("DELETE FROM `playlists`");
      _db.execSQL("DELETE FROM `playlist_tracks`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoritesDao.class, FavoritesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoritesDao favoritesDao() {
    if (_favoritesDao != null) {
      return _favoritesDao;
    } else {
      synchronized(this) {
        if(_favoritesDao == null) {
          _favoritesDao = new FavoritesDao_Impl(this);
        }
        return _favoritesDao;
      }
    }
  }

  @Override
  public PlaylistDao playlistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }
}
