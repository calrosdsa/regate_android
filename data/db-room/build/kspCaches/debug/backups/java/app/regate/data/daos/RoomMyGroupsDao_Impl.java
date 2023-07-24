package app.regate.data.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Grupo;
import app.regate.models.MyGroups;
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
public final class RoomMyGroupsDao_Impl extends RoomMyGroupsDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<MyGroups> __deletionAdapterOfMyGroups;

  private final EntityDeletionOrUpdateAdapter<MyGroups> __updateAdapterOfMyGroups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<MyGroups> __upsertionAdapterOfMyGroups;

  public RoomMyGroupsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfMyGroups = new EntityDeletionOrUpdateAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `my_groups` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMyGroups = new EntityDeletionOrUpdateAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `my_groups` SET `id` = ?,`group_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroup_id());
        statement.bindLong(3, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from my_groups";
        return _query;
      }
    };
    this.__upsertionAdapterOfMyGroups = new EntityUpsertionAdapter<MyGroups>(new EntityInsertionAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `my_groups` (`id`,`group_id`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroup_id());
      }
    }, new EntityDeletionOrUpdateAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `my_groups` SET `id` = ?,`group_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroup_id());
        statement.bindLong(3, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final MyGroups entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfMyGroups.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final MyGroups entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMyGroups.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public Object upsert(final MyGroups entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfMyGroups.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final MyGroups[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMyGroups.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends MyGroups> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMyGroups.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Grupo>> observeUserGroups() {
    final String _sql = "select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"my_groups",
        "grupos"}, new Callable<List<Grupo>>() {
      @Override
      @NonNull
      public List<Grupo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final List<Grupo> _result = new ArrayList<Grupo>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Grupo _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Instant _tmpCreated_at;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
            final String _tmpPhoto;
            if (_cursor.isNull(_cursorIndexOfPhoto)) {
              _tmpPhoto = null;
            } else {
              _tmpPhoto = _cursor.getString(_cursorIndexOfPhoto);
            }
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            _item = new Grupo(_tmpId,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpProfile_id);
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
