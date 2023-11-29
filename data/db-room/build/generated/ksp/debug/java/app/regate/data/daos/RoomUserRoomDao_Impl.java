package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import app.regate.models.UserRoom;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomUserRoomDao_Impl extends RoomUserRoomDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserRoom> __insertionAdapterOfUserRoom;

  private final EntityDeletionOrUpdateAdapter<UserRoom> __deletionAdapterOfUserRoom;

  private final EntityDeletionOrUpdateAdapter<UserRoom> __updateAdapterOfUserRoom;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUserRoom;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUserIsOut;

  private final EntityUpsertionAdapter<UserRoom> __upsertionAdapterOfUserRoom;

  public RoomUserRoomDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserRoom = new EntityInsertionAdapter<UserRoom>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `user_room` (`id`,`profile_id`,`sala_id`,`is_admin`,`is_out`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserRoom entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getSala_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__deletionAdapterOfUserRoom = new EntityDeletionOrUpdateAdapter<UserRoom>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `user_room` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserRoom entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfUserRoom = new EntityDeletionOrUpdateAdapter<UserRoom>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `user_room` SET `id` = ?,`profile_id` = ?,`sala_id` = ?,`is_admin` = ?,`is_out` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserRoom entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getSala_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteUserRoom = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from user_room where id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUserIsOut = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update user_room set is_out = ? where id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfUserRoom = new EntityUpsertionAdapter<UserRoom>(new EntityInsertionAdapter<UserRoom>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `user_room` (`id`,`profile_id`,`sala_id`,`is_admin`,`is_out`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserRoom entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getSala_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    }, new EntityDeletionOrUpdateAdapter<UserRoom>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `user_room` SET `id` = ?,`profile_id` = ?,`sala_id` = ?,`is_admin` = ?,`is_out` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserRoom entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getSala_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final UserRoom entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserRoom.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends UserRoom> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserRoom.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final UserRoom entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfUserRoom.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final UserRoom entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserRoom.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUserRoom(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUserRoom.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUserRoom.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateUserIsOut(final long id, final boolean isOut,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUserIsOut.acquire();
        int _argIndex = 1;
        final int _tmp = isOut ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateUserIsOut.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final UserRoom entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfUserRoom.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final UserRoom[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUserRoom.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends UserRoom> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUserRoom.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getUserRoom(final long salaId, final long profileId,
      final Continuation<? super UserRoom> continuation) {
    final String _sql = "select * from user_room where sala_id = ? and profile_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, salaId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, profileId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserRoom>() {
      @Override
      @Nullable
      public UserRoom call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSalaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sala_id");
          final int _cursorIndexOfIsAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "is_admin");
          final int _cursorIndexOfIsOut = CursorUtil.getColumnIndexOrThrow(_cursor, "is_out");
          final UserRoom _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            final long _tmpSala_id;
            _tmpSala_id = _cursor.getLong(_cursorIndexOfSalaId);
            final boolean _tmpIs_admin;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAdmin);
            _tmpIs_admin = _tmp != 0;
            final boolean _tmpIs_out;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOut);
            _tmpIs_out = _tmp_1 != 0;
            _result = new UserRoom(_tmpId,_tmpProfile_id,_tmpSala_id,_tmpIs_admin,_tmpIs_out);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
