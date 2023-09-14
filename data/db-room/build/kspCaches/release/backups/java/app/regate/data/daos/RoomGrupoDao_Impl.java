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
public final class RoomGrupoDao_Impl extends RoomGrupoDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<Grupo> __deletionAdapterOfGrupo;

  private final EntityDeletionOrUpdateAdapter<Grupo> __updateAdapterOfGrupo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<Grupo> __upsertionAdapterOfGrupo;

  public RoomGrupoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfGrupo = new EntityDeletionOrUpdateAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `grupos` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGrupo = new EntityDeletionOrUpdateAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `grupos` SET `id` = ?,`name` = ?,`description` = ?,`created_at` = ?,`photo` = ?,`profile_id` = ?,`visibility` = ?,`last_message` = ?,`last_message_created` = ?,`messages_count` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoto());
        }
        statement.bindLong(6, entity.getProfile_id());
        statement.bindLong(7, entity.getVisibility());
        statement.bindString(8, entity.getLast_message());
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_1);
        }
        statement.bindLong(10, entity.getMessages_count());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from grupos";
        return _query;
      }
    };
    this.__upsertionAdapterOfGrupo = new EntityUpsertionAdapter<Grupo>(new EntityInsertionAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `grupos` (`id`,`name`,`description`,`created_at`,`photo`,`profile_id`,`visibility`,`last_message`,`last_message_created`,`messages_count`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoto());
        }
        statement.bindLong(6, entity.getProfile_id());
        statement.bindLong(7, entity.getVisibility());
        statement.bindString(8, entity.getLast_message());
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_1);
        }
        statement.bindLong(10, entity.getMessages_count());
      }
    }, new EntityDeletionOrUpdateAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `grupos` SET `id` = ?,`name` = ?,`description` = ?,`created_at` = ?,`photo` = ?,`profile_id` = ?,`visibility` = ?,`last_message` = ?,`last_message_created` = ?,`messages_count` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoto());
        }
        statement.bindLong(6, entity.getProfile_id());
        statement.bindLong(7, entity.getVisibility());
        statement.bindString(8, entity.getLast_message());
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp_1 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_1);
        }
        statement.bindLong(10, entity.getMessages_count());
        statement.bindLong(11, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final Grupo entity, final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfGrupo.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Grupo entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGrupo.handle(entity);
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
  public Object upsert(final Grupo entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfGrupo.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Grupo[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfGrupo.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Grupo> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfGrupo.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<Grupo> observeGrupo(final long id) {
    final String _sql = "select * from grupos where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"grupos"}, new Callable<Grupo>() {
      @Override
      @NonNull
      public Grupo call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
            final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
            final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
            final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
            final Grupo _result;
            if (_cursor.moveToFirst()) {
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
              final int _tmpVisibility;
              _tmpVisibility = _cursor.getInt(_cursorIndexOfVisibility);
              final String _tmpLast_message;
              _tmpLast_message = _cursor.getString(_cursorIndexOfLastMessage);
              final Instant _tmpLast_message_created;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfLastMessageCreated)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfLastMessageCreated);
              }
              _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_1);
              final int _tmpMessages_count;
              _tmpMessages_count = _cursor.getInt(_cursorIndexOfMessagesCount);
              _result = new Grupo(_tmpId,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpProfile_id,_tmpVisibility,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count);
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
  public Flow<List<Grupo>> observeGrupos() {
    final String _sql = "select * from grupos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"grupos"}, new Callable<List<Grupo>>() {
      @Override
      @NonNull
      public List<Grupo> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
            final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
            final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
            final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
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
              final int _tmpVisibility;
              _tmpVisibility = _cursor.getInt(_cursorIndexOfVisibility);
              final String _tmpLast_message;
              _tmpLast_message = _cursor.getString(_cursorIndexOfLastMessage);
              final Instant _tmpLast_message_created;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfLastMessageCreated)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfLastMessageCreated);
              }
              _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_1);
              final int _tmpMessages_count;
              _tmpMessages_count = _cursor.getInt(_cursorIndexOfMessagesCount);
              _item = new Grupo(_tmpId,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpProfile_id,_tmpVisibility,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count);
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
  public Flow<List<Grupo>> observeUserGroups() {
    final String _sql = "select  *  from grupos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"grupos"}, new Callable<List<Grupo>>() {
      @Override
      @NonNull
      public List<Grupo> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
            final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
            final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
            final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
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
              final int _tmpVisibility;
              _tmpVisibility = _cursor.getInt(_cursorIndexOfVisibility);
              final String _tmpLast_message;
              _tmpLast_message = _cursor.getString(_cursorIndexOfLastMessage);
              final Instant _tmpLast_message_created;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfLastMessageCreated)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfLastMessageCreated);
              }
              _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_1);
              final int _tmpMessages_count;
              _tmpMessages_count = _cursor.getInt(_cursorIndexOfMessagesCount);
              _item = new Grupo(_tmpId,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpProfile_id,_tmpVisibility,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count);
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
  public Grupo getGrupo(final long id) {
    final String _sql = "select * from grupos where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
      final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
      final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
      final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
      final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
      final Grupo _result;
      if (_cursor.moveToFirst()) {
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
        final int _tmpVisibility;
        _tmpVisibility = _cursor.getInt(_cursorIndexOfVisibility);
        final String _tmpLast_message;
        _tmpLast_message = _cursor.getString(_cursorIndexOfLastMessage);
        final Instant _tmpLast_message_created;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfLastMessageCreated)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfLastMessageCreated);
        }
        _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_1);
        final int _tmpMessages_count;
        _tmpMessages_count = _cursor.getInt(_cursorIndexOfMessagesCount);
        _result = new Grupo(_tmpId,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpProfile_id,_tmpVisibility,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
