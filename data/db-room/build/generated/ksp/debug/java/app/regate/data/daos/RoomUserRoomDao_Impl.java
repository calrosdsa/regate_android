package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.UserProfileGrupoAndSala;
import app.regate.models.UserRoom;
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

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomUserRoomDao_Impl extends RoomUserRoomDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserRoom> __insertionAdapterOfUserRoom;

  private final EntityDeletionOrUpdateAdapter<UserRoom> __deletionAdapterOfUserRoom;

  private final EntityDeletionOrUpdateAdapter<UserRoom> __updateAdapterOfUserRoom;

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
  public Flow<List<UserProfileGrupoAndSala>> observeUsersRoom(final long id) {
    final String _sql = "\n"
            + "        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.is_out,ug.id as id from user_room as ug\n"
            + "        inner join profiles as p on p.id = ug.profile_id\n"
            + "        where ug.sala_id = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"user_room",
        "profiles"}, new Callable<List<UserProfileGrupoAndSala>>() {
      @Override
      @NonNull
      public List<UserProfileGrupoAndSala> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfProfileId = 0;
            final int _cursorIndexOfNombre = 1;
            final int _cursorIndexOfApellido = 2;
            final int _cursorIndexOfProfilePhoto = 3;
            final int _cursorIndexOfIsAdmin = 4;
            final int _cursorIndexOfIsOut = 5;
            final int _cursorIndexOfId = 6;
            final List<UserProfileGrupoAndSala> _result = new ArrayList<UserProfileGrupoAndSala>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final UserProfileGrupoAndSala _item;
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              final String _tmpNombre;
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
              final String _tmpApellido;
              if (_cursor.isNull(_cursorIndexOfApellido)) {
                _tmpApellido = null;
              } else {
                _tmpApellido = _cursor.getString(_cursorIndexOfApellido);
              }
              final String _tmpProfile_photo;
              if (_cursor.isNull(_cursorIndexOfProfilePhoto)) {
                _tmpProfile_photo = null;
              } else {
                _tmpProfile_photo = _cursor.getString(_cursorIndexOfProfilePhoto);
              }
              final boolean _tmpIs_admin;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsAdmin);
              _tmpIs_admin = _tmp != 0;
              final boolean _tmpIs_out;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsOut);
              _tmpIs_out = _tmp_1 != 0;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              _item = new UserProfileGrupoAndSala(_tmpProfile_id,_tmpNombre,_tmpApellido,_tmpProfile_photo,_tmpIs_admin,_tmpIs_out,_tmpId);
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
  public Object getUsersCount(final boolean isOut, final long roomId,
      final Continuation<? super Integer> continuation) {
    final String _sql = "select count(*) from user_room where sala_id = ? and is_out = ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, roomId);
    _argIndex = 2;
    final int _tmp = isOut ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
          } else {
            _result = 0;
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
