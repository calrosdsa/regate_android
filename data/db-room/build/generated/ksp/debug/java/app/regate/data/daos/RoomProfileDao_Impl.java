package app.regate.data.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Profile;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class RoomProfileDao_Impl extends RoomProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Profile> __insertionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __deletionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __updateAdapterOfProfile;

  private final EntityUpsertionAdapter<Profile> __upsertionAdapterOfProfile;

  public RoomProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfile = new EntityInsertionAdapter<Profile>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `profiles` (`id`,`uuid`,`user_id`,`email`,`profile_photo`,`nombre`,`apellido`,`created_at`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Profile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        if (entity.getUser_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getUser_id());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getProfile_photo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getProfile_photo());
        }
        statement.bindString(6, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getApellido());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
      }
    };
    this.__deletionAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `profiles` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Profile entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `profiles` SET `id` = ?,`uuid` = ?,`user_id` = ?,`email` = ?,`profile_photo` = ?,`nombre` = ?,`apellido` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Profile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        if (entity.getUser_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getUser_id());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getProfile_photo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getProfile_photo());
        }
        statement.bindString(6, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getApellido());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__upsertionAdapterOfProfile = new EntityUpsertionAdapter<Profile>(new EntityInsertionAdapter<Profile>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `profiles` (`id`,`uuid`,`user_id`,`email`,`profile_photo`,`nombre`,`apellido`,`created_at`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Profile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        if (entity.getUser_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getUser_id());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getProfile_photo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getProfile_photo());
        }
        statement.bindString(6, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getApellido());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `profiles` SET `id` = ?,`uuid` = ?,`user_id` = ?,`email` = ?,`profile_photo` = ?,`nombre` = ?,`apellido` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Profile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        if (entity.getUser_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getUser_id());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getProfile_photo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getProfile_photo());
        }
        statement.bindString(6, entity.getNombre());
        if (entity.getApellido() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getApellido());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        statement.bindLong(9, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Profile entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProfile.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Profile> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProfile.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Profile entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfProfile.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Profile entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProfile.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final Profile entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfProfile.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Profile[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfProfile.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Profile> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfProfile.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<Profile> observeProfile(final long id) {
    final String _sql = "select * from profiles where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"profiles"}, new Callable<Profile>() {
      @Override
      @NonNull
      public Profile call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfProfilePhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_photo");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "apellido");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final Profile _result;
            if (_cursor.moveToFirst()) {
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpUuid;
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
              final Long _tmpUser_id;
              if (_cursor.isNull(_cursorIndexOfUserId)) {
                _tmpUser_id = null;
              } else {
                _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
              }
              final String _tmpEmail;
              if (_cursor.isNull(_cursorIndexOfEmail)) {
                _tmpEmail = null;
              } else {
                _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
              }
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
              final Instant _tmpCreated_at;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              _result = new Profile(_tmpId,_tmpUuid,_tmpUser_id,_tmpEmail,_tmpProfile_photo,_tmpNombre,_tmpApellido,_tmpCreated_at);
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
  public Flow<List<Profile>> observeProfileSalas(final List<Long> ids) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("select * from profiles where id in (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, true, new String[] {"profiles"}, new Callable<List<Profile>>() {
      @Override
      @NonNull
      public List<Profile> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfProfilePhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_photo");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfApellido = CursorUtil.getColumnIndexOrThrow(_cursor, "apellido");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final List<Profile> _result = new ArrayList<Profile>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Profile _item_1;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpUuid;
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
              final Long _tmpUser_id;
              if (_cursor.isNull(_cursorIndexOfUserId)) {
                _tmpUser_id = null;
              } else {
                _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
              }
              final String _tmpEmail;
              if (_cursor.isNull(_cursorIndexOfEmail)) {
                _tmpEmail = null;
              } else {
                _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
              }
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
              final Instant _tmpCreated_at;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              _item_1 = new Profile(_tmpId,_tmpUuid,_tmpUser_id,_tmpEmail,_tmpProfile_photo,_tmpNombre,_tmpApellido,_tmpCreated_at);
              _result.add(_item_1);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
