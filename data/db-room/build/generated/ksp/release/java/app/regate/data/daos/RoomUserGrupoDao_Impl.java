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
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.UserProfileGrupo;
import app.regate.models.UserGrupo;
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

  private final EntityDeletionOrUpdateAdapter<UserGrupo> __deletionAdapterOfUserGrupo;

  private final EntityDeletionOrUpdateAdapter<UserGrupo> __updateAdapterOfUserGrupo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUsers;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUserGroup;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUser;

  private final EntityUpsertionAdapter<UserGrupo> __upsertionAdapterOfUserGrupo;

  public RoomUserGrupoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
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
        return "UPDATE OR ABORT `user_grupo` SET `id` = ?,`profile_id` = ?,`grupo_id` = ?,`is_admin` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getId());
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
        final String _query = "DELETE FROM user_grupo where id = ?";
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
    this.__upsertionAdapterOfUserGrupo = new EntityUpsertionAdapter<UserGrupo>(new EntityInsertionAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `user_grupo` (`id`,`profile_id`,`grupo_id`,`is_admin`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    }, new EntityDeletionOrUpdateAdapter<UserGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `user_grupo` SET `id` = ?,`profile_id` = ?,`grupo_id` = ?,`is_admin` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserGrupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindLong(3, entity.getGrupo_id());
        final int _tmp = entity.is_admin() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getId());
      }
    });
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
  public Flow<List<UserProfileGrupo>> observeUsersGrupo(final long id) {
    final String _sql = "\n"
            + "        select p.id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.id as user_group_id from user_grupo as ug\n"
            + "        inner join profiles as p on p.id = ug.profile_id\n"
            + "        where ug.grupo_id = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"user_grupo",
        "profiles"}, new Callable<List<UserProfileGrupo>>() {
      @Override
      @NonNull
      public List<UserProfileGrupo> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = 0;
            final int _cursorIndexOfNombre = 1;
            final int _cursorIndexOfApellido = 2;
            final int _cursorIndexOfProfilePhoto = 3;
            final int _cursorIndexOfIsAdmin = 4;
            final int _cursorIndexOfUserGroupId = 5;
            final List<UserProfileGrupo> _result = new ArrayList<UserProfileGrupo>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final UserProfileGrupo _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
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
              final long _tmpUser_group_id;
              _tmpUser_group_id = _cursor.getLong(_cursorIndexOfUserGroupId);
              _item = new UserProfileGrupo(_tmpId,_tmpNombre,_tmpApellido,_tmpProfile_photo,_tmpIs_admin,_tmpUser_group_id);
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