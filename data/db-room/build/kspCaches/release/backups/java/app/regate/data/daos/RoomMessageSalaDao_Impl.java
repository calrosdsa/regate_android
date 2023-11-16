package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.MessageSalaWithProfile;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.MessageSala;
import app.regate.models.Profile;
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
import kotlinx.datetime.Instant;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomMessageSalaDao_Impl extends RoomMessageSalaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageSala> __insertionAdapterOfMessageSala;

  private final EntityDeletionOrUpdateAdapter<MessageSala> __deletionAdapterOfMessageSala;

  private final EntityDeletionOrUpdateAdapter<MessageSala> __updateAdapterOfMessageSala;

  private final EntityUpsertionAdapter<MessageSala> __upsertionAdapterOfMessageSala;

  public RoomMessageSalaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageSala = new EntityInsertionAdapter<MessageSala>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `message_sala` (`id`,`sala_id`,`content`,`created_at`,`profile_id`,`reply_to`,`sended`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageSala entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSala_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
      }
    };
    this.__deletionAdapterOfMessageSala = new EntityDeletionOrUpdateAdapter<MessageSala>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `message_sala` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageSala entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMessageSala = new EntityDeletionOrUpdateAdapter<MessageSala>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `message_sala` SET `id` = ?,`sala_id` = ?,`content` = ?,`created_at` = ?,`profile_id` = ?,`reply_to` = ?,`sended` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageSala entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSala_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getId());
      }
    };
    this.__upsertionAdapterOfMessageSala = new EntityUpsertionAdapter<MessageSala>(new EntityInsertionAdapter<MessageSala>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `message_sala` (`id`,`sala_id`,`content`,`created_at`,`profile_id`,`reply_to`,`sended`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageSala entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSala_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
      }
    }, new EntityDeletionOrUpdateAdapter<MessageSala>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `message_sala` SET `id` = ?,`sala_id` = ?,`content` = ?,`created_at` = ?,`profile_id` = ?,`reply_to` = ?,`sended` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageSala entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSala_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final MessageSala entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageSala.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends MessageSala> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageSala.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final MessageSala entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfMessageSala.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final MessageSala entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageSala.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final MessageSala entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfMessageSala.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final MessageSala[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessageSala.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends MessageSala> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessageSala.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public PagingSource<Integer, MessageSalaWithProfile> observeMessages(final long id) {
    final String _sql = "SELECT * FROM message_sala where sala_id = ? ORDER BY datetime(created_at) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return new LimitOffsetPagingSource<MessageSalaWithProfile>(_statement, __db, "profiles", "message_sala") {
      @Override
      @NonNull
      protected List<MessageSalaWithProfile> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfSalaId = CursorUtil.getColumnIndexOrThrow(cursor, "sala_id");
        final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(cursor, "content");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(cursor, "profile_id");
        final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(cursor, "reply_to");
        final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(cursor, "sended");
        final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
        final LongSparseArray<MessageSala> _collectionReply = new LongSparseArray<MessageSala>();
        while (cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = cursor.getLong(_cursorIndexOfProfileId);
          _collectionProfile.put(_tmpKey, null);
          final Long _tmpKey_1;
          if (cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = cursor.getLong(_cursorIndexOfReplyTo);
          }
          if (_tmpKey_1 != null) {
            _collectionReply.put(_tmpKey_1, null);
          }
        }
        cursor.moveToPosition(-1);
        __fetchRelationshipprofilesAsappRegateModelsProfile(_collectionProfile);
        __fetchRelationshipmessageSalaAsappRegateModelsMessageSala(_collectionReply);
        final List<MessageSalaWithProfile> _result = new ArrayList<MessageSalaWithProfile>(cursor.getCount());
        while (cursor.moveToNext()) {
          final MessageSalaWithProfile _item;
          final MessageSala _tmpMessage;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final long _tmpSala_id;
          _tmpSala_id = cursor.getLong(_cursorIndexOfSalaId);
          final String _tmpContent;
          _tmpContent = cursor.getString(_cursorIndexOfContent);
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
          final long _tmpProfile_id;
          _tmpProfile_id = cursor.getLong(_cursorIndexOfProfileId);
          final Long _tmpReply_to;
          if (cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpReply_to = null;
          } else {
            _tmpReply_to = cursor.getLong(_cursorIndexOfReplyTo);
          }
          final boolean _tmpSended;
          final int _tmp_2;
          _tmp_2 = cursor.getInt(_cursorIndexOfSended);
          _tmpSended = _tmp_2 != 0;
          _tmpMessage = new MessageSala(_tmpId,_tmpSala_id,_tmpContent,_tmpCreated_at,_tmpProfile_id,_tmpReply_to,_tmpSended);
          final Profile _tmpProfile;
          final long _tmpKey_2;
          _tmpKey_2 = cursor.getLong(_cursorIndexOfProfileId);
          _tmpProfile = _collectionProfile.get(_tmpKey_2);
          final MessageSala _tmpReply;
          final Long _tmpKey_3;
          if (cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpKey_3 = null;
          } else {
            _tmpKey_3 = cursor.getLong(_cursorIndexOfReplyTo);
          }
          if (_tmpKey_3 != null) {
            _tmpReply = _collectionReply.get(_tmpKey_3);
          } else {
            _tmpReply = null;
          }
          _item = new MessageSalaWithProfile();
          _item.message = _tmpMessage;
          _item.setProfile(_tmpProfile);
          _item.setReply(_tmpReply);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Object getReplyMessage(final long id,
      final Continuation<? super MessageSalaWithProfile> continuation) {
    final String _sql = "SELECT * FROM message_sala where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<MessageSalaWithProfile>() {
      @Override
      @NonNull
      public MessageSalaWithProfile call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfSalaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sala_id");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
            final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
            final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
            final LongSparseArray<MessageSala> _collectionReply = new LongSparseArray<MessageSala>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfProfileId);
              _collectionProfile.put(_tmpKey, null);
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfReplyTo)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfReplyTo);
              }
              if (_tmpKey_1 != null) {
                _collectionReply.put(_tmpKey_1, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipprofilesAsappRegateModelsProfile(_collectionProfile);
            __fetchRelationshipmessageSalaAsappRegateModelsMessageSala(_collectionReply);
            final MessageSalaWithProfile _result;
            if (_cursor.moveToFirst()) {
              final MessageSala _tmpMessage;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpSala_id;
              _tmpSala_id = _cursor.getLong(_cursorIndexOfSalaId);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final Instant _tmpCreated_at;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpCreated_at = _tmp_1;
              }
              final long _tmpProfile_id;
              _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
              final Long _tmpReply_to;
              if (_cursor.isNull(_cursorIndexOfReplyTo)) {
                _tmpReply_to = null;
              } else {
                _tmpReply_to = _cursor.getLong(_cursorIndexOfReplyTo);
              }
              final boolean _tmpSended;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfSended);
              _tmpSended = _tmp_2 != 0;
              _tmpMessage = new MessageSala(_tmpId,_tmpSala_id,_tmpContent,_tmpCreated_at,_tmpProfile_id,_tmpReply_to,_tmpSended);
              final Profile _tmpProfile;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfProfileId);
              _tmpProfile = _collectionProfile.get(_tmpKey_2);
              final MessageSala _tmpReply;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfReplyTo)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfReplyTo);
              }
              if (_tmpKey_3 != null) {
                _tmpReply = _collectionReply.get(_tmpKey_3);
              } else {
                _tmpReply = null;
              }
              _result = new MessageSalaWithProfile();
              _result.message = _tmpMessage;
              _result.setProfile(_tmpProfile);
              _result.setReply(_tmpReply);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public List<MessageSalaWithProfile> getMessages(final long id) {
    final String _sql = "SELECT * FROM message_sala where sala_id = ? ORDER BY datetime(created_at) DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfSalaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sala_id");
        final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
        final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
        final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
        final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
        final LongSparseArray<MessageSala> _collectionReply = new LongSparseArray<MessageSala>();
        while (_cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = _cursor.getLong(_cursorIndexOfProfileId);
          _collectionProfile.put(_tmpKey, null);
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfReplyTo);
          }
          if (_tmpKey_1 != null) {
            _collectionReply.put(_tmpKey_1, null);
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipprofilesAsappRegateModelsProfile(_collectionProfile);
        __fetchRelationshipmessageSalaAsappRegateModelsMessageSala(_collectionReply);
        final List<MessageSalaWithProfile> _result = new ArrayList<MessageSalaWithProfile>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final MessageSalaWithProfile _item;
          final MessageSala _tmpMessage;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpSala_id;
          _tmpSala_id = _cursor.getLong(_cursorIndexOfSalaId);
          final String _tmpContent;
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
          final Instant _tmpCreated_at;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
          }
          final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpCreated_at = _tmp_1;
          }
          final long _tmpProfile_id;
          _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
          final Long _tmpReply_to;
          if (_cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpReply_to = null;
          } else {
            _tmpReply_to = _cursor.getLong(_cursorIndexOfReplyTo);
          }
          final boolean _tmpSended;
          final int _tmp_2;
          _tmp_2 = _cursor.getInt(_cursorIndexOfSended);
          _tmpSended = _tmp_2 != 0;
          _tmpMessage = new MessageSala(_tmpId,_tmpSala_id,_tmpContent,_tmpCreated_at,_tmpProfile_id,_tmpReply_to,_tmpSended);
          final Profile _tmpProfile;
          final long _tmpKey_2;
          _tmpKey_2 = _cursor.getLong(_cursorIndexOfProfileId);
          _tmpProfile = _collectionProfile.get(_tmpKey_2);
          final MessageSala _tmpReply;
          final Long _tmpKey_3;
          if (_cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpKey_3 = null;
          } else {
            _tmpKey_3 = _cursor.getLong(_cursorIndexOfReplyTo);
          }
          if (_tmpKey_3 != null) {
            _tmpReply = _collectionReply.get(_tmpKey_3);
          } else {
            _tmpReply = null;
          }
          _item = new MessageSalaWithProfile();
          _item.message = _tmpMessage;
          _item.setProfile(_tmpProfile);
          _item.setReply(_tmpReply);
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Object getUnSendedMessage(final long profileId, final long salaId,
      final Continuation<? super List<MessageSala>> continuation) {
    final String _sql = "select *  from message_sala where profile_id = ? and sended = 0 and sala_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, salaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageSala>>() {
      @Override
      @NonNull
      public List<MessageSala> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSalaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sala_id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
          final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
          final List<MessageSala> _result = new ArrayList<MessageSala>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageSala _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSala_id;
            _tmpSala_id = _cursor.getLong(_cursorIndexOfSalaId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final Instant _tmpCreated_at;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
            } else {
              _tmpCreated_at = _tmp_1;
            }
            final long _tmpProfile_id;
            _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
            final Long _tmpReply_to;
            if (_cursor.isNull(_cursorIndexOfReplyTo)) {
              _tmpReply_to = null;
            } else {
              _tmpReply_to = _cursor.getLong(_cursorIndexOfReplyTo);
            }
            final boolean _tmpSended;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSended);
            _tmpSended = _tmp_2 != 0;
            _item = new MessageSala(_tmpId,_tmpSala_id,_tmpContent,_tmpCreated_at,_tmpProfile_id,_tmpReply_to,_tmpSended);
            _result.add(_item);
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

  private void __fetchRelationshipmessageSalaAsappRegateModelsMessageSala(
      @NonNull final LongSparseArray<MessageSala> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipmessageSalaAsappRegateModelsMessageSala(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`sala_id`,`content`,`created_at`,`profile_id`,`reply_to`,`sended` FROM `message_sala` WHERE `id` IN (");
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
      final int _cursorIndexOfSalaId = 1;
      final int _cursorIndexOfContent = 2;
      final int _cursorIndexOfCreatedAt = 3;
      final int _cursorIndexOfProfileId = 4;
      final int _cursorIndexOfReplyTo = 5;
      final int _cursorIndexOfSended = 6;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final MessageSala _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpSala_id;
          _tmpSala_id = _cursor.getLong(_cursorIndexOfSalaId);
          final String _tmpContent;
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
          final Instant _tmpCreated_at;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfCreatedAt);
          }
          final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpCreated_at = _tmp_1;
          }
          final long _tmpProfile_id;
          _tmpProfile_id = _cursor.getLong(_cursorIndexOfProfileId);
          final Long _tmpReply_to;
          if (_cursor.isNull(_cursorIndexOfReplyTo)) {
            _tmpReply_to = null;
          } else {
            _tmpReply_to = _cursor.getLong(_cursorIndexOfReplyTo);
          }
          final boolean _tmpSended;
          final int _tmp_2;
          _tmp_2 = _cursor.getInt(_cursorIndexOfSended);
          _tmpSended = _tmp_2 != 0;
          _item_1 = new MessageSala(_tmpId,_tmpSala_id,_tmpContent,_tmpCreated_at,_tmpProfile_id,_tmpReply_to,_tmpSended);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
