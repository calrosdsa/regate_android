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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.models.User;
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
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomUserDao_Impl extends RoomUserDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUser;

  private final EntityUpsertionAdapter<User> __upsertionAdapterOfUser;

  public RoomUserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `users` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`user_id` = ?,`email` = ?,`estado` = ?,`username` = ?,`profile_photo` = ?,`nombre` = ?,`apellido` = ?,`profile_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        if (entity.getProfile_photo() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getProfile_photo());
        }
        statement.bindString(7, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getApellido());
        }
        statement.bindLong(9, entity.getProfile_id());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM users";
        return _query;
      }
    };
    this.__upsertionAdapterOfUser = new EntityUpsertionAdapter<User>(new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `users` (`id`,`user_id`,`email`,`estado`,`username`,`profile_photo`,`nombre`,`apellido`,`profile_id`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        if (entity.getProfile_photo() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getProfile_photo());
        }
        statement.bindString(7, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getApellido());
        }
        statement.bindLong(9, entity.getProfile_id());
      }
    }, new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `users` SET `id` = ?,`user_id` = ?,`email` = ?,`estado` = ?,`username` = ?,`profile_photo` = ?,`nombre` = ?,`apellido` = ?,`profile_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        if (entity.getProfile_photo() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getProfile_photo());
        }
        statement.bindString(7, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getApellido());
        }
        statement.bindLong(9, entity.getProfile_id());
        statement.bindLong(10, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final User entity, final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfUser.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final User entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUser.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUser(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUser.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUser.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final User entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfUser.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final User[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUser.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends User> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUser.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<User> observeUser() {
    final String _sql = "SELECT * FROM users limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"users"}, new Callable<User>() {
      @Override
      @NonNull
      public User call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
            final int _cursorIndexOfProfilePhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_photo");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "apellido");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final User _result;
            if (_cursor.moveToFirst()) {
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpUser_id;
              _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
              final String _tmpEmail;
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
              final int _tmpEstado;
              _tmpEstado = _cursor.getInt(_cursorIndexOfEstado);
              final String _tmpUsername;
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
              final String _tmpProfile_photo;
              if (_cursor.isNull(_cursorIndexOfProfilePhoto)) {
                _tmpProfile_photo = null;
              } else {
                _tmpProfile_photo = _cursor.getString(_cursorIndexOfProfilePhoto);
              }
              final String _tmpNombre;
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
              final String _tmpApellido;
              if (_cursor.isNull(_cursorIndexOfApellido)) {
                _tmpApellido = null;
              } else {
                _tmpApellido = _cursor.getString(_cursorIndexOfApellido);
              }
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              _result = new User(_tmpId,_tmpUser_id,_tmpEmail,_tmpEstado,_tmpUsername,_tmpProfile_photo,_tmpNombre,_tmpApellido,_tmpProfile_id);
            } else {
              _result = null;
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
  public Object getUser(final long id, final Continuation<? super User> continuation) {
    final String _sql = "SELECT * FROM USERS WHERE id = ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @NonNull
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfProfilePhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_photo");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "apellido");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final User _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUser_id;
            _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final int _tmpEstado;
            _tmpEstado = _cursor.getInt(_cursorIndexOfEstado);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpProfile_photo;
            if (_cursor.isNull(_cursorIndexOfProfilePhoto)) {
              _tmpProfile_photo = null;
            } else {
              _tmpProfile_photo = _cursor.getString(_cursorIndexOfProfilePhoto);
            }
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpApellido;
            if (_cursor.isNull(_cursorIndexOfApellido)) {
              _tmpApellido = null;
            } else {
              _tmpApellido = _cursor.getString(_cursorIndexOfApellido);
            }
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            _result = new User(_tmpId,_tmpUser_id,_tmpEmail,_tmpEstado,_tmpUsername,_tmpProfile_photo,_tmpNombre,_tmpApellido,_tmpProfile_id);
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
