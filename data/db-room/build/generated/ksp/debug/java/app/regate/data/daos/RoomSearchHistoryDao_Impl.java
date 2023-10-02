package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.SearchHistory;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Instant;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomSearchHistoryDao_Impl extends RoomSearchHistoryDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<SearchHistory> __deletionAdapterOfSearchHistory;

  private final EntityDeletionOrUpdateAdapter<SearchHistory> __updateAdapterOfSearchHistory;

  private final EntityUpsertionAdapter<SearchHistory> __upsertionAdapterOfSearchHistory;

  public RoomSearchHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfSearchHistory = new EntityDeletionOrUpdateAdapter<SearchHistory>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `search_history` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SearchHistory entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSearchHistory = new EntityDeletionOrUpdateAdapter<SearchHistory>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `search_history` SET `id` = ?,`query` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SearchHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuery());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        statement.bindLong(4, entity.getId());
      }
    };
    this.__upsertionAdapterOfSearchHistory = new EntityUpsertionAdapter<SearchHistory>(new EntityInsertionAdapter<SearchHistory>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `search_history` (`id`,`query`,`created_at`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SearchHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuery());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<SearchHistory>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `search_history` SET `id` = ?,`query` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SearchHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuery());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        statement.bindLong(4, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final SearchHistory entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfSearchHistory.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final SearchHistory entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSearchHistory.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final SearchHistory entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfSearchHistory.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final SearchHistory[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfSearchHistory.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends SearchHistory> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfSearchHistory.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<SearchHistory>> observeRecentHistory(final int page, final int size) {
    final String _sql = "select * from search_history order by created_at desc limit ? offset (? * ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, size);
    _argIndex = 2;
    _statement.bindLong(_argIndex, size);
    _argIndex = 3;
    _statement.bindLong(_argIndex, page);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"search_history"}, new Callable<List<SearchHistory>>() {
      @Override
      @NonNull
      public List<SearchHistory> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfQuery = CursorUtil.getColumnIndexOrThrow(_cursor, "query");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final List<SearchHistory> _result = new ArrayList<SearchHistory>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final SearchHistory _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpQuery;
              _tmpQuery = _cursor.getString(_cursorIndexOfQuery);
              final Instant _tmpCreated_at;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              _item = new SearchHistory(_tmpId,_tmpQuery,_tmpCreated_at);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public PagingSource<Integer, SearchHistory> observePaginationRecentHistory(final int page,
      final int size) {
    final String _sql = "select * from search_history order by created_at desc  limit ? offset (? * ?) ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, size);
    _argIndex = 2;
    _statement.bindLong(_argIndex, size);
    _argIndex = 3;
    _statement.bindLong(_argIndex, page);
    return new LimitOffsetPagingSource<SearchHistory>(_statement, __db, "search_history") {
      @Override
      @NonNull
      protected List<SearchHistory> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfQuery = CursorUtil.getColumnIndexOrThrow(cursor, "query");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final List<SearchHistory> _result = new ArrayList<SearchHistory>(cursor.getCount());
        while (cursor.moveToNext()) {
          final SearchHistory _item;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final String _tmpQuery;
          _tmpQuery = cursor.getString(_cursorIndexOfQuery);
          final Instant _tmpCreated_at;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfCreatedAt);
          }
          _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          _item = new SearchHistory(_tmpId,_tmpQuery,_tmpCreated_at);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Object getHistorySearch(final int page, final int size,
      final Continuation<? super List<SearchHistory>> continuation) {
    final String _sql = "select * from search_history order by created_at desc  limit ? offset (? * ?) ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, size);
    _argIndex = 2;
    _statement.bindLong(_argIndex, size);
    _argIndex = 3;
    _statement.bindLong(_argIndex, page);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<SearchHistory>>() {
      @Override
      @NonNull
      public List<SearchHistory> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfQuery = CursorUtil.getColumnIndexOrThrow(_cursor, "query");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final List<SearchHistory> _result = new ArrayList<SearchHistory>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final SearchHistory _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpQuery;
              _tmpQuery = _cursor.getString(_cursorIndexOfQuery);
              final Instant _tmpCreated_at;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              _item = new SearchHistory(_tmpId,_tmpQuery,_tmpCreated_at);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
