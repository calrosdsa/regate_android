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
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto;
import app.regate.models.grupo.UserGrupo;
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
public final class RoomUserGrupoDao_Impl extends RoomUserGrupoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserGrupo> __insertionAdapterOfUserGrupo;

  private final EntityDeletionOrUpdateAdapter<UserGrupo> __deletionAdapterOfUserGrupo;

  private final EntityDeletionOrUpdateAdapter<UserGrupo> __updateAdapterOfUserGrupo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUsers;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUserGroup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUsersGroup;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUser;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUserIsOut;

  private final EntityUpsertionAdapter<UserGrupo> __upsertionAdapterOfUserGrupo;

  public RoomUserGrupoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserGrupo = new EntityInsertionAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `user_grupo` (`id`,`profile_id`,`grupo_id`,`is_admin`,`is_out`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__deletionAdapterOfUserGrupo = new EntityDeletionOrUpdateAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `user_grupo` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfUserGrupo = new EntityDeletionOrUpdateAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `user_grupo` SET `id` = ?,`profile_id` = ?,`grupo_id` = ?,`is_admin` = ?,`is_out` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteUsers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_grupo where grupo_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteUserGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_grupo where  id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteUsersGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_grupo where  grupo_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_grupo set is_admin = ? where id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUserIsOut = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update user_grupo set is_out = ? where id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfUserGrupo = new EntityUpsertionAdapter<UserGrupo>(new EntityInsertionAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `user_grupo` (`id`,`profile_id`,`grupo_id`,`is_admin`,`is_out`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    }, new EntityDeletionOrUpdateAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `user_grupo` SET `id` = ?,`profile_id` = ?,`grupo_id` = ?,`is_admin` = ?,`is_out` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.is_out() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final UserGrupo entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserGrupo.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends UserGrupo> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserGrupo.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final UserGrupo entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfUserGrupo.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final UserGrupo entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserGrupo.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUsers(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUsers.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUsers.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUserGroup(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUserGroup.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUserGroup.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUsersGroup(final long groupId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUsersGroup.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, groupId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUsersGroup.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateUser(final long id, final boolean status,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUser.acquire();
        int _argIndex = 1;
        final int _tmp = status ? 1 : 0;
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
          __preparedStmtOfUpdateUser.release(_stmt);
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
  public Object upsert(final UserGrupo entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfUserGrupo.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final UserGrupo[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUserGrupo.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends UserGrupo> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfUserGrupo.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<UserProfileGrupoAndSalaDto>> observeUsersGrupo(final long id) {
    final String _sql = "\n"
            + "        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.is_out,ug.id as id,\n"
            + "        ug.grupo_id as parent_id,(2) as type_chat  from user_grupo as ug\n"
            + "        inner join profiles as p on p.id = ug.profile_id\n"
            + "        where ug.grupo_id = ? and is_out = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"user_grupo",
        "profiles"}, new Callable<List<UserProfileGrupoAndSalaDto>>() {
      @Override
      @NonNull
      public List<UserProfileGrupoAndSalaDto> call() throws Exception {
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
            final int _cursorIndexOfParentId = 7;
            final int _cursorIndexOfTypeChat = 8;
            final List<UserProfileGrupoAndSalaDto> _result = new ArrayList<UserProfileGrupoAndSalaDto>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final UserProfileGrupoAndSalaDto _item;
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
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final int _tmpType_chat;
              _tmpType_chat = _cursor.getInt(_cursorIndexOfTypeChat);
              _item = new UserProfileGrupoAndSalaDto(_tmpProfile_id,_tmpNombre,_tmpApellido,_tmpProfile_photo,_tmpIs_admin,_tmpIs_out,_tmpId,_tmpParent_id,_tmpType_chat);
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
  public Object getUserGroup(final long groupId, final long profileId,
      final Continuation<? super UserGrupo> continuation) {
    final String _sql = "select * from user_grupo where grupo_id = ? and profile_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, profileId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserGrupo>() {
      @Override
      @Nullable
      public UserGrupo call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfGrupoId = CursorUtil.getColumnIndexOrThrow(_cursor, "grupo_id");
          final int _cursorIndexOfIsAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "is_admin");
          final int _cursorIndexOfIsOut = CursorUtil.getColumnIndexOrThrow(_cursor, "is_out");
          final UserGrupo _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            final long _tmpGrupo_id;
            _tmpGrupo_id = _cursor.getLong(_cursorIndexOfGrupoId);
            final boolean _tmpIs_admin;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAdmin);
            _tmpIs_admin = _tmp != 0;
            final boolean _tmpIs_out;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOut);
            _tmpIs_out = _tmp_1 != 0;
            _result = new UserGrupo(_tmpId,_tmpProfile_id,_tmpGrupo_id,_tmpIs_admin,_tmpIs_out);
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

  @Override
  public Flow<List<UserProfileGrupoAndSalaDto>> observeUsersRoom(final long id) {
    final String _sql = "\n"
            + "        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ur.is_admin,ur.is_out,ur.id as id, \n"
            + "        ur.sala_id as parent_id,(1) as type_chat from user_room as ur\n"
            + "        inner join profiles as p on p.id = ur.profile_id\n"
            + "        where ur.sala_id = ? and is_out = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"user_room",
        "profiles"}, new Callable<List<UserProfileGrupoAndSalaDto>>() {
      @Override
      @NonNull
      public List<UserProfileGrupoAndSalaDto> call() throws Exception {
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
            final int _cursorIndexOfParentId = 7;
            final int _cursorIndexOfTypeChat = 8;
            final List<UserProfileGrupoAndSalaDto> _result = new ArrayList<UserProfileGrupoAndSalaDto>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final UserProfileGrupoAndSalaDto _item;
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
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final int _tmpType_chat;
              _tmpType_chat = _cursor.getInt(_cursorIndexOfTypeChat);
              _item = new UserProfileGrupoAndSalaDto(_tmpProfile_id,_tmpNombre,_tmpApellido,_tmpProfile_photo,_tmpIs_admin,_tmpIs_out,_tmpId,_tmpParent_id,_tmpType_chat);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
