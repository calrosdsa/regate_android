package app.regate.data.daos;

import android.database.Cursor;
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
import app.regate.data.db.AppTypeConverters;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado;
import app.regate.models.grupo.Grupo;
import app.regate.models.grupo.MyGroups;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
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

  private final EntityInsertionAdapter<MyGroups> __insertionAdapterOfMyGroups;

  private final EntityDeletionOrUpdateAdapter<MyGroups> __deletionAdapterOfMyGroups;

  private final EntityDeletionOrUpdateAdapter<MyGroups> __updateAdapterOfMyGroups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMyGroups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByGroupId;

  private final EntityUpsertionAdapter<MyGroups> __upsertionAdapterOfMyGroups;

  public RoomMyGroupsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMyGroups = new EntityInsertionAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `my_groups` (`id`,`request_estado`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = AppTypeConverters.INSTANCE.fromGrupoRequestEstado(entity.getRequest_estado());
        statement.bindLong(2, _tmp);
      }
    };
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
        return "UPDATE OR ABORT `my_groups` SET `id` = ?,`request_estado` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = AppTypeConverters.INSTANCE.fromGrupoRequestEstado(entity.getRequest_estado());
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMyGroups = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from  my_groups where request_estado = ?";
        return _query;
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
    this.__preparedStmtOfDeleteByGroupId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from my_groups where id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfMyGroups = new EntityUpsertionAdapter<MyGroups>(new EntityInsertionAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `my_groups` (`id`,`request_estado`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = AppTypeConverters.INSTANCE.fromGrupoRequestEstado(entity.getRequest_estado());
        statement.bindLong(2, _tmp);
      }
    }, new EntityDeletionOrUpdateAdapter<MyGroups>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `my_groups` SET `id` = ?,`request_estado` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyGroups entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = AppTypeConverters.INSTANCE.fromGrupoRequestEstado(entity.getRequest_estado());
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final MyGroups entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMyGroups.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends MyGroups> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMyGroups.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
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
  public Object deleteMyGroups(final int estado, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMyGroups.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, estado);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteMyGroups.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteByGroupId(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByGroupId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteByGroupId.release(_stmt);
        }
      }
    }, continuation);
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
    final String _sql = "\n"
            + "        select g.* from my_groups as ug inner join grupos as g on g.id = ug.id\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"my_groups",
        "grupos"}, new Callable<List<Grupo>>() {
      @Override
      @NonNull
      public List<Grupo> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
            final int _cursorIndexOfMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "members");
            final List<Grupo> _result = new ArrayList<Grupo>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Grupo _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpUuid;
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
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
              final boolean _tmpIs_visible;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsVisible);
              _tmpIs_visible = _tmp_1 != 0;
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              final int _tmpVisibility;
              _tmpVisibility = _cursor.getInt(_cursorIndexOfVisibility);
              final int _tmpMembers;
              _tmpMembers = _cursor.getInt(_cursorIndexOfMembers);
              _item = new Grupo(_tmpId,_tmpUuid,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpIs_visible,_tmpProfile_id,_tmpVisibility,_tmpMembers);
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
  public Flow<List<MyGroups>> observeMyGroups() {
    final String _sql = "select * from my_groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"my_groups"}, new Callable<List<MyGroups>>() {
      @Override
      @NonNull
      public List<MyGroups> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfRequestEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "request_estado");
            final List<MyGroups> _result = new ArrayList<MyGroups>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MyGroups _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final GrupoRequestEstado _tmpRequest_estado;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfRequestEstado);
              final Integer _tmp_1;
              _tmp_1 = _tmp;
              final GrupoRequestEstado _tmp_2 = AppTypeConverters.INSTANCE.toGrupoRequestEstado(_tmp_1);
              if (_tmp_2 == null) {
                throw new IllegalStateException("Expected non-null app.regate.data.dto.empresa.grupo.GrupoRequestEstado, but it was null.");
              } else {
                _tmpRequest_estado = _tmp_2;
              }
              _item = new MyGroups(_tmpId,_tmpRequest_estado);
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
  public Flow<MyGroups> observeMyGroupById(final long grupoId) {
    final String _sql = "select * from my_groups where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, grupoId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"my_groups"}, new Callable<MyGroups>() {
      @Override
      @Nullable
      public MyGroups call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfRequestEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "request_estado");
            final MyGroups _result;
            if (_cursor.moveToFirst()) {
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final GrupoRequestEstado _tmpRequest_estado;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfRequestEstado);
              final Integer _tmp_1;
              _tmp_1 = _tmp;
              final GrupoRequestEstado _tmp_2 = AppTypeConverters.INSTANCE.toGrupoRequestEstado(_tmp_1);
              if (_tmp_2 == null) {
                throw new IllegalStateException("Expected non-null app.regate.data.dto.empresa.grupo.GrupoRequestEstado, but it was null.");
              } else {
                _tmpRequest_estado = _tmp_2;
              }
              _result = new MyGroups(_tmpId,_tmpRequest_estado);
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
}
