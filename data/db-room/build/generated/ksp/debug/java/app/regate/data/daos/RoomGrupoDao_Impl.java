package app.regate.data.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.grupo.UserInvitation;
import app.regate.compoundmodels.grupo.UserInvitationGrupo;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.grupo.Grupo;
import app.regate.models.grupo.InvitationGrupo;
import app.regate.models.user.Profile;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
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
public final class RoomGrupoDao_Impl extends RoomGrupoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Grupo> __insertionAdapterOfGrupo;

  private final EntityDeletionOrUpdateAdapter<Grupo> __deletionAdapterOfGrupo;

  private final EntityDeletionOrUpdateAdapter<Grupo> __updateAdapterOfGrupo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdateInvitationEstado;

  private final SharedSQLiteStatement __preparedStmtOfDeleteInvitations;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUserInvitations;

  private final EntityUpsertionAdapter<Grupo> __upsertionAdapterOfGrupo;

  private final EntityUpsertionAdapter<InvitationGrupo> __upsertionAdapterOfInvitationGrupo;

  public RoomGrupoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGrupo = new EntityInsertionAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `grupos` (`id`,`uuid`,`name`,`description`,`created_at`,`photo`,`is_visible`,`profile_id`,`visibility`,`members`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoto());
        }
        final int _tmp_1 = entity.is_visible() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getProfile_id());
        statement.bindLong(9, entity.getVisibility());
        statement.bindLong(10, entity.getMembers());
      }
    };
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
        return "UPDATE OR ABORT `grupos` SET `id` = ?,`uuid` = ?,`name` = ?,`description` = ?,`created_at` = ?,`photo` = ?,`is_visible` = ?,`profile_id` = ?,`visibility` = ?,`members` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoto());
        }
        final int _tmp_1 = entity.is_visible() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getProfile_id());
        statement.bindLong(9, entity.getVisibility());
        statement.bindLong(10, entity.getMembers());
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
    this.__preparedStmtOfUpdateInvitationEstado = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update invitation_grupo set estado = ? where grupo_id = ? and profile_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteInvitations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from invitation_grupo where grupo_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteUserInvitations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from invitation_grupo where profile_id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfGrupo = new EntityUpsertionAdapter<Grupo>(new EntityInsertionAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `grupos` (`id`,`uuid`,`name`,`description`,`created_at`,`photo`,`is_visible`,`profile_id`,`visibility`,`members`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoto());
        }
        final int _tmp_1 = entity.is_visible() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getProfile_id());
        statement.bindLong(9, entity.getVisibility());
        statement.bindLong(10, entity.getMembers());
      }
    }, new EntityDeletionOrUpdateAdapter<Grupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `grupos` SET `id` = ?,`uuid` = ?,`name` = ?,`description` = ?,`created_at` = ?,`photo` = ?,`is_visible` = ?,`profile_id` = ?,`visibility` = ?,`members` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Grupo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUuid());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoto());
        }
        final int _tmp_1 = entity.is_visible() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getProfile_id());
        statement.bindLong(9, entity.getVisibility());
        statement.bindLong(10, entity.getMembers());
        statement.bindLong(11, entity.getId());
      }
    });
    this.__upsertionAdapterOfInvitationGrupo = new EntityUpsertionAdapter<InvitationGrupo>(new EntityInsertionAdapter<InvitationGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `invitation_grupo` (`profile_id`,`grupo_id`,`estado`,`created_at`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InvitationGrupo entity) {
        statement.bindLong(1, entity.getProfile_id());
        statement.bindLong(2, entity.getGrupo_id());
        statement.bindLong(3, entity.getEstado());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<InvitationGrupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `invitation_grupo` SET `profile_id` = ?,`grupo_id` = ?,`estado` = ?,`created_at` = ? WHERE `profile_id` = ? AND `grupo_id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InvitationGrupo entity) {
        statement.bindLong(1, entity.getProfile_id());
        statement.bindLong(2, entity.getGrupo_id());
        statement.bindLong(3, entity.getEstado());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getProfile_id());
        statement.bindLong(6, entity.getGrupo_id());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Grupo entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGrupo.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Grupo> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGrupo.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
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
  public Object updateInvitationEstado(final long grupoId, final long profileId, final int estado,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateInvitationEstado.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, estado);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, grupoId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, profileId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateInvitationEstado.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteInvitations(final long grupoId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteInvitations.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, grupoId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteInvitations.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUserInvitations(final long profileId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUserInvitations.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, profileId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteUserInvitations.release(_stmt);
        }
      }
    }, continuation);
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
  public Object insertInvitation(final InvitationGrupo invitation,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfInvitationGrupo.upsert(invitation);
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
            final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
            final int _cursorIndexOfMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "members");
            final Grupo _result;
            if (_cursor.moveToFirst()) {
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
              _result = new Grupo(_tmpId,_tmpUuid,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpIs_visible,_tmpProfile_id,_tmpVisibility,_tmpMembers);
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
  public PagingSource<Integer, Grupo> observePaginationGroups() {
    final String _sql = "select * from grupos ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new LimitOffsetPagingSource<Grupo>(_statement, __db, "grupos") {
      @Override
      @NonNull
      protected List<Grupo> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(cursor, "uuid");
        final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(cursor, "name");
        final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(cursor, "photo");
        final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(cursor, "is_visible");
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(cursor, "profile_id");
        final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(cursor, "visibility");
        final int _cursorIndexOfMembers = CursorUtil.getColumnIndexOrThrow(cursor, "members");
        final List<Grupo> _result = new ArrayList<Grupo>(cursor.getCount());
        while (cursor.moveToNext()) {
          final Grupo _item;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final String _tmpUuid;
          _tmpUuid = cursor.getString(_cursorIndexOfUuid);
          final String _tmpName;
          _tmpName = cursor.getString(_cursorIndexOfName);
          final String _tmpDescription;
          if (cursor.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = cursor.getString(_cursorIndexOfDescription);
          }
          final Instant _tmpCreated_at;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfCreatedAt);
          }
          _tmpCreated_at = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          final String _tmpPhoto;
          if (cursor.isNull(_cursorIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = cursor.getString(_cursorIndexOfPhoto);
          }
          final boolean _tmpIs_visible;
          final int _tmp_1;
          _tmp_1 = cursor.getInt(_cursorIndexOfIsVisible);
          _tmpIs_visible = _tmp_1 != 0;
          final long _tmpProfile_id;
          _tmpProfile_id = cursor.getLong(_cursorIndexOfProfileId);
          final int _tmpVisibility;
          _tmpVisibility = cursor.getInt(_cursorIndexOfVisibility);
          final int _tmpMembers;
          _tmpMembers = cursor.getInt(_cursorIndexOfMembers);
          _item = new Grupo(_tmpId,_tmpUuid,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpIs_visible,_tmpProfile_id,_tmpVisibility,_tmpMembers);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Flow<List<Grupo>> observeGrupos(final int size) {
    final String _sql = "select * from grupos limit ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, size);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"grupos"}, new Callable<List<Grupo>>() {
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
  public PagingSource<Integer, UserInvitationGrupo> observeInvitations(final long grupoId) {
    final String _sql = "select * from invitation_grupo where grupo_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, grupoId);
    return new LimitOffsetPagingSource<UserInvitationGrupo>(_statement, __db, "profiles", "invitation_grupo") {
      @Override
      @NonNull
      protected List<UserInvitationGrupo> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(cursor, "profile_id");
        final int _cursorIndexOfGrupoId = CursorUtil.getColumnIndexOrThrow(cursor, "grupo_id");
        final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(cursor, "estado");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
        while (cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = cursor.getLong(_cursorIndexOfProfileId);
          _collectionProfile.put(_tmpKey, null);
        }
        cursor.moveToPosition(-1);
        __fetchRelationshipprofilesAsappRegateModelsUserProfile(_collectionProfile);
        final List<UserInvitationGrupo> _result = new ArrayList<UserInvitationGrupo>(cursor.getCount());
        while (cursor.moveToNext()) {
          final UserInvitationGrupo _item;
          final InvitationGrupo _tmpInvitation;
          final long _tmpProfile_id;
          _tmpProfile_id = cursor.getLong(_cursorIndexOfProfileId);
          final long _tmpGrupo_id;
          _tmpGrupo_id = cursor.getLong(_cursorIndexOfGrupoId);
          final int _tmpEstado;
          _tmpEstado = cursor.getInt(_cursorIndexOfEstado);
          final Instant _tmpCreated_at;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfCreatedAt);
          }
          final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpCreated_at = _tmp_1;
          }
          _tmpInvitation = new InvitationGrupo(_tmpProfile_id,_tmpGrupo_id,_tmpEstado,_tmpCreated_at);
          final Profile _tmpProfile;
          final long _tmpKey_1;
          _tmpKey_1 = cursor.getLong(_cursorIndexOfProfileId);
          _tmpProfile = _collectionProfile.get(_tmpKey_1);
          _item = new UserInvitationGrupo();
          _item.invitation = _tmpInvitation;
          _item.setProfile(_tmpProfile);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public PagingSource<Integer, UserInvitation> observeUserInvitations(final long profileId) {
    final String _sql = "select * from invitation_grupo where profile_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    return new LimitOffsetPagingSource<UserInvitation>(_statement, __db, "grupos", "invitation_grupo") {
      @Override
      @NonNull
      protected List<UserInvitation> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(cursor, "profile_id");
        final int _cursorIndexOfGrupoId = CursorUtil.getColumnIndexOrThrow(cursor, "grupo_id");
        final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(cursor, "estado");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final LongSparseArray<Grupo> _collectionGrupo = new LongSparseArray<Grupo>();
        while (cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = cursor.getLong(_cursorIndexOfGrupoId);
          _collectionGrupo.put(_tmpKey, null);
        }
        cursor.moveToPosition(-1);
        __fetchRelationshipgruposAsappRegateModelsGrupoGrupo(_collectionGrupo);
        final List<UserInvitation> _result = new ArrayList<UserInvitation>(cursor.getCount());
        while (cursor.moveToNext()) {
          final UserInvitation _item;
          final InvitationGrupo _tmpInvitation;
          final long _tmpProfile_id;
          _tmpProfile_id = cursor.getLong(_cursorIndexOfProfileId);
          final long _tmpGrupo_id;
          _tmpGrupo_id = cursor.getLong(_cursorIndexOfGrupoId);
          final int _tmpEstado;
          _tmpEstado = cursor.getInt(_cursorIndexOfEstado);
          final Instant _tmpCreated_at;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfCreatedAt);
          }
          final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpCreated_at = _tmp_1;
          }
          _tmpInvitation = new InvitationGrupo(_tmpProfile_id,_tmpGrupo_id,_tmpEstado,_tmpCreated_at);
          final Grupo _tmpGrupo;
          final long _tmpKey_1;
          _tmpKey_1 = cursor.getLong(_cursorIndexOfGrupoId);
          _tmpGrupo = _collectionGrupo.get(_tmpKey_1);
          _item = new UserInvitation();
          _item.invitation = _tmpInvitation;
          _item.setGrupo(_tmpGrupo);
          _result.add(_item);
        }
        return _result;
      }
    };
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
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
      final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
      final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
      final int _cursorIndexOfMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "members");
      final Grupo _result;
      if (_cursor.moveToFirst()) {
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
        _result = new Grupo(_tmpId,_tmpUuid,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpIs_visible,_tmpProfile_id,_tmpVisibility,_tmpMembers);
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

  private void __fetchRelationshipprofilesAsappRegateModelsUserProfile(
      @NonNull final LongSparseArray<Profile> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipprofilesAsappRegateModelsUserProfile(map);
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

  private void __fetchRelationshipgruposAsappRegateModelsGrupoGrupo(
      @NonNull final LongSparseArray<Grupo> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipgruposAsappRegateModelsGrupoGrupo(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`uuid`,`name`,`description`,`created_at`,`photo`,`is_visible`,`profile_id`,`visibility`,`members` FROM `grupos` WHERE `id` IN (");
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
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfDescription = 3;
      final int _cursorIndexOfCreatedAt = 4;
      final int _cursorIndexOfPhoto = 5;
      final int _cursorIndexOfIsVisible = 6;
      final int _cursorIndexOfProfileId = 7;
      final int _cursorIndexOfVisibility = 8;
      final int _cursorIndexOfMembers = 9;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Grupo _item_1;
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
          _item_1 = new Grupo(_tmpId,_tmpUuid,_tmpName,_tmpDescription,_tmpCreated_at,_tmpPhoto,_tmpIs_visible,_tmpProfile_id,_tmpVisibility,_tmpMembers);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
