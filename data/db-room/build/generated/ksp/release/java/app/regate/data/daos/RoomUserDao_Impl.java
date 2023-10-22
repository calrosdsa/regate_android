package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.UserProfile;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Profile;
import app.regate.models.account.User;
import app.regate.models.account.UserBalance;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Instant;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomUserDao_Impl extends RoomUserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserBalance> __insertionAdapterOfUserBalance;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUser;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUserBalance;

  private final EntityUpsertionAdapter<User> __upsertionAdapterOfUser;

  public RoomUserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserBalance = new EntityInsertionAdapter<UserBalance>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_balance` (`balance_id`,`profile_id`,`coins`) VALUES (?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserBalance entity) {
        statement.bindLong(1, entity.getBalance_id());
        statement.bindLong(2, entity.getProfile_id());
        statement.bindDouble(3, entity.getCoins());
      }
    };
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
        return "UPDATE OR ABORT `users` SET `id` = ?,`user_id` = ?,`email` = ?,`estado` = ?,`username` = ?,`profile_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        statement.bindLong(6, entity.getProfile_id());
        statement.bindLong(7, entity.getId());
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
    this.__preparedStmtOfUpdateUserBalance = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        update user_balance set coins = case when ? then coins + ? else coins - ? end \n"
                + "        where profile_id = ?\n"
                + "            ";
        return _query;
      }
    };
    this.__upsertionAdapterOfUser = new EntityUpsertionAdapter<User>(new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `users` (`id`,`user_id`,`email`,`estado`,`username`,`profile_id`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        statement.bindLong(6, entity.getProfile_id());
      }
    }, new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `users` SET `id` = ?,`user_id` = ?,`email` = ?,`estado` = ?,`username` = ?,`profile_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUser_id());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getEstado());
        statement.bindString(5, entity.getUsername());
        statement.bindLong(6, entity.getProfile_id());
        statement.bindLong(7, entity.getId());
      }
    });
  }

  @Override
  public Object insetUserBalance(final UserBalance entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserBalance.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
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
  public void updateUserBalance(final long profileId, final double amount,
      final boolean shouldAdd) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUserBalance.acquire();
    int _argIndex = 1;
    final int _tmp = shouldAdd ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    _stmt.bindDouble(_argIndex, amount);
    _argIndex = 3;
    _stmt.bindDouble(_argIndex, amount);
    _argIndex = 4;
    _stmt.bindLong(_argIndex, profileId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateUserBalance.release(_stmt);
    }
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
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              _result = new User(_tmpId,_tmpUser_id,_tmpEmail,_tmpEstado,_tmpUsername,_tmpProfile_id);
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
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            _result = new User(_tmpId,_tmpUser_id,_tmpEmail,_tmpEstado,_tmpUsername,_tmpProfile_id);
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
  public Flow<UserBalance> observeUserBalance() {
    final String _sql = "SELECT * FROM user_balance  limit 1 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"user_balance"}, new Callable<UserBalance>() {
      @Override
      @NonNull
      public UserBalance call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfBalanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "balance_id");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfCoins = CursorUtil.getColumnIndexOrThrow(_cursor, "coins");
            final UserBalance _result;
            if (_cursor.moveToFirst()) {
              final long _tmpBalance_id;
              _tmpBalance_id = _cursor.getLong(_cursorIndexOfBalanceId);
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              final double _tmpCoins;
              _tmpCoins = _cursor.getDouble(_cursorIndexOfCoins);
              _result = new UserBalance(_tmpBalance_id,_tmpProfile_id,_tmpCoins);
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
  public Flow<UserProfile> observeUserAndProfile() {
    final String _sql = "SELECT * FROM USERS  limit 1 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"profiles",
        "USERS"}, new Callable<UserProfile>() {
      @Override
      @NonNull
      public UserProfile call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
            final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfProfileId);
              _collectionProfile.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipprofilesAsappRegateModelsProfile(_collectionProfile);
            final UserProfile _result;
            if (_cursor.moveToFirst()) {
              final User _tmpUser;
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
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              _tmpUser = new User(_tmpId,_tmpUser_id,_tmpEmail,_tmpEstado,_tmpUsername,_tmpProfile_id);
              final Profile _tmpProfile;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfProfileId);
              _tmpProfile = _collectionProfile.get(_tmpKey_1);
              _result = new UserProfile();
              _result.user = _tmpUser;
              _result.setProfile(_tmpProfile);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipprofilesAsappRegateModelsProfile(
      @NonNull final LongSparseArray<Profile> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipprofilesAsappRegateModelsProfile(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`uuid`,`user_id`,`email`,`profile_photo`,`nombre`,`apellido`,`created_at` FROM `profiles` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfUuid = 1;
      final int _cursorIndexOfUserId = 2;
      final int _cursorIndexOfEmail = 3;
      final int _cursorIndexOfProfilePhoto = 4;
      final int _cursorIndexOfNombre = 5;
      final int _cursorIndexOfApellido = 6;
      final int _cursorIndexOfCreatedAt = 7;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
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
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
