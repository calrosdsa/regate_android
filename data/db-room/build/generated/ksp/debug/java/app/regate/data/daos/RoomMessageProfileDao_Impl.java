package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import app.regate.compoundmodels.MessageProfile;
import app.regate.compoundmodels.MessageWithChat;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Message;
import app.regate.models.Profile;
import app.regate.models.chat.Chat;
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
public final class RoomMessageProfileDao_Impl extends RoomMessageProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Message> __insertionAdapterOfMessage;

  private final EntityDeletionOrUpdateAdapter<Message> __deletionAdapterOfMessage;

  private final EntityDeletionOrUpdateAdapter<Message> __updateAdapterOfMessage;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUnreadMessages;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSendedMessage;

  private final SharedSQLiteStatement __preparedStmtOfUpdatedPrimaryKey;

  private final EntityUpsertionAdapter<Message> __upsertionAdapterOfMessage;

  public RoomMessageProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessage = new EntityInsertionAdapter<Message>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `messages` (`id`,`chat_id`,`content`,`data`,`created_at`,`type_message`,`profile_id`,`reply_to`,`sended`,`readed`,`parent_id`,`is_user`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Message entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChat_id());
        statement.bindString(3, entity.getContent());
        if (entity.getData() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getData());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getType_message());
        statement.bindLong(7, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.getReaded() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getParent_id());
        final int _tmp_3 = entity.is_user() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
      }
    };
    this.__deletionAdapterOfMessage = new EntityDeletionOrUpdateAdapter<Message>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `messages` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Message entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMessage = new EntityDeletionOrUpdateAdapter<Message>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `messages` SET `id` = ?,`chat_id` = ?,`content` = ?,`data` = ?,`created_at` = ?,`type_message` = ?,`profile_id` = ?,`reply_to` = ?,`sended` = ?,`readed` = ?,`parent_id` = ?,`is_user` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Message entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChat_id());
        statement.bindString(3, entity.getContent());
        if (entity.getData() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getData());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getType_message());
        statement.bindLong(7, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.getReaded() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getParent_id());
        final int _tmp_3 = entity.is_user() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateUnreadMessages = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update messages set readed = 1 where chat_id= ? and readed = 0";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSendedMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update messages set sended = 1,id = ? where id = ? and sended = 0";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatedPrimaryKey = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update messages set id = ? ,sended = 1,readed = 1 where id= ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfMessage = new EntityUpsertionAdapter<Message>(new EntityInsertionAdapter<Message>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `messages` (`id`,`chat_id`,`content`,`data`,`created_at`,`type_message`,`profile_id`,`reply_to`,`sended`,`readed`,`parent_id`,`is_user`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Message entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChat_id());
        statement.bindString(3, entity.getContent());
        if (entity.getData() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getData());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getType_message());
        statement.bindLong(7, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.getReaded() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getParent_id());
        final int _tmp_3 = entity.is_user() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
      }
    }, new EntityDeletionOrUpdateAdapter<Message>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `messages` SET `id` = ?,`chat_id` = ?,`content` = ?,`data` = ?,`created_at` = ?,`type_message` = ?,`profile_id` = ?,`reply_to` = ?,`sended` = ?,`readed` = ?,`parent_id` = ?,`is_user` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Message entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChat_id());
        statement.bindString(3, entity.getContent());
        if (entity.getData() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getData());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getType_message());
        statement.bindLong(7, entity.getProfile_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final int _tmp_2 = entity.getReaded() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        statement.bindLong(11, entity.getParent_id());
        final int _tmp_3 = entity.is_user() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
        statement.bindLong(13, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Message entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessage.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Message> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessage.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Message entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfMessage.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Message entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessage.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateUnreadMessages(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUnreadMessages.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateUnreadMessages.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateSendedMessage(final long id, final long newId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSendedMessage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateSendedMessage.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updatedPrimaryKey(final long id, final long newId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatedPrimaryKey.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdatedPrimaryKey.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final Message entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfMessage.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Message[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessage.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Message> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessage.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public PagingSource<Integer, MessageProfile> observeMessages(final long id) {
    final String _sql = "SELECT * FROM messages where chat_id = ? ORDER BY datetime(created_at) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return new LimitOffsetPagingSource<MessageProfile>(_statement, __db, "profiles", "messages") {
      @Override
      @NonNull
      protected List<MessageProfile> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(cursor, "chat_id");
        final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(cursor, "content");
        final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(cursor, "data");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final int _cursorIndexOfTypeMessage = CursorUtil.getColumnIndexOrThrow(cursor, "type_message");
        final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(cursor, "profile_id");
        final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(cursor, "reply_to");
        final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(cursor, "sended");
        final int _cursorIndexOfReaded = CursorUtil.getColumnIndexOrThrow(cursor, "readed");
        final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(cursor, "parent_id");
        final int _cursorIndexOfIsUser = CursorUtil.getColumnIndexOrThrow(cursor, "is_user");
        final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
        final LongSparseArray<Message> _collectionReply = new LongSparseArray<Message>();
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
        __fetchRelationshipmessagesAsappRegateModelsMessage(_collectionReply);
        final List<MessageProfile> _result = new ArrayList<MessageProfile>(cursor.getCount());
        while (cursor.moveToNext()) {
          final MessageProfile _item;
          final Message _tmpMessage;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final long _tmpChat_id;
          _tmpChat_id = cursor.getLong(_cursorIndexOfChatId);
          final String _tmpContent;
          _tmpContent = cursor.getString(_cursorIndexOfContent);
          final String _tmpData;
          if (cursor.isNull(_cursorIndexOfData)) {
            _tmpData = null;
          } else {
            _tmpData = cursor.getString(_cursorIndexOfData);
          }
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
          final int _tmpType_message;
          _tmpType_message = cursor.getInt(_cursorIndexOfTypeMessage);
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
          final boolean _tmpReaded;
          final int _tmp_3;
          _tmp_3 = cursor.getInt(_cursorIndexOfReaded);
          _tmpReaded = _tmp_3 != 0;
          final long _tmpParent_id;
          _tmpParent_id = cursor.getLong(_cursorIndexOfParentId);
          final boolean _tmpIs_user;
          final int _tmp_4;
          _tmp_4 = cursor.getInt(_cursorIndexOfIsUser);
          _tmpIs_user = _tmp_4 != 0;
          _tmpMessage = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
          final Profile _tmpProfile;
          final long _tmpKey_2;
          _tmpKey_2 = cursor.getLong(_cursorIndexOfProfileId);
          _tmpProfile = _collectionProfile.get(_tmpKey_2);
          final Message _tmpReply;
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
          _item = new MessageProfile();
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
      final Continuation<? super MessageProfile> continuation) {
    final String _sql = "SELECT * FROM messages where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<MessageProfile>() {
      @Override
      @NonNull
      public MessageProfile call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfTypeMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "type_message");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
            final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
            final int _cursorIndexOfReaded = CursorUtil.getColumnIndexOrThrow(_cursor, "readed");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
            final int _cursorIndexOfIsUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_user");
            final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
            final LongSparseArray<Message> _collectionReply = new LongSparseArray<Message>();
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
            __fetchRelationshipmessagesAsappRegateModelsMessage(_collectionReply);
            final MessageProfile _result;
            if (_cursor.moveToFirst()) {
              final Message _tmpMessage;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpChat_id;
              _tmpChat_id = _cursor.getLong(_cursorIndexOfChatId);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpData;
              if (_cursor.isNull(_cursorIndexOfData)) {
                _tmpData = null;
              } else {
                _tmpData = _cursor.getString(_cursorIndexOfData);
              }
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
              final int _tmpType_message;
              _tmpType_message = _cursor.getInt(_cursorIndexOfTypeMessage);
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
              final boolean _tmpReaded;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfReaded);
              _tmpReaded = _tmp_3 != 0;
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final boolean _tmpIs_user;
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsUser);
              _tmpIs_user = _tmp_4 != 0;
              _tmpMessage = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
              final Profile _tmpProfile;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfProfileId);
              _tmpProfile = _collectionProfile.get(_tmpKey_2);
              final Message _tmpReply;
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
              _result = new MessageProfile();
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
  public Flow<List<MessageProfile>> getMessages(final long id) {
    final String _sql = "SELECT * FROM messages where chat_id = ? ORDER BY datetime(created_at) DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"profiles",
        "messages"}, new Callable<List<MessageProfile>>() {
      @Override
      @NonNull
      public List<MessageProfile> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfTypeMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "type_message");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
            final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
            final int _cursorIndexOfReaded = CursorUtil.getColumnIndexOrThrow(_cursor, "readed");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
            final int _cursorIndexOfIsUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_user");
            final LongSparseArray<Profile> _collectionProfile = new LongSparseArray<Profile>();
            final LongSparseArray<Message> _collectionReply = new LongSparseArray<Message>();
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
            __fetchRelationshipmessagesAsappRegateModelsMessage(_collectionReply);
            final List<MessageProfile> _result = new ArrayList<MessageProfile>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MessageProfile _item;
              final Message _tmpMessage;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpChat_id;
              _tmpChat_id = _cursor.getLong(_cursorIndexOfChatId);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpData;
              if (_cursor.isNull(_cursorIndexOfData)) {
                _tmpData = null;
              } else {
                _tmpData = _cursor.getString(_cursorIndexOfData);
              }
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
              final int _tmpType_message;
              _tmpType_message = _cursor.getInt(_cursorIndexOfTypeMessage);
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
              final boolean _tmpReaded;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfReaded);
              _tmpReaded = _tmp_3 != 0;
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final boolean _tmpIs_user;
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsUser);
              _tmpIs_user = _tmp_4 != 0;
              _tmpMessage = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
              final Profile _tmpProfile;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfProfileId);
              _tmpProfile = _collectionProfile.get(_tmpKey_2);
              final Message _tmpReply;
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
              _item = new MessageProfile();
              _item.message = _tmpMessage;
              _item.setProfile(_tmpProfile);
              _item.setReply(_tmpReply);
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
  public Object getUnSendedMessage(final Continuation<? super List<MessageWithChat>> continuation) {
    final String _sql = "select *  from messages where sended = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<MessageWithChat>>() {
      @Override
      @NonNull
      public List<MessageWithChat> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfTypeMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "type_message");
            final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
            final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
            final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
            final int _cursorIndexOfReaded = CursorUtil.getColumnIndexOrThrow(_cursor, "readed");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
            final int _cursorIndexOfIsUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_user");
            final LongSparseArray<Chat> _collectionChat = new LongSparseArray<Chat>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfChatId);
              _collectionChat.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchatAsappRegateModelsChatChat(_collectionChat);
            final List<MessageWithChat> _result = new ArrayList<MessageWithChat>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final MessageWithChat _item;
              final Message _tmpMessage;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpChat_id;
              _tmpChat_id = _cursor.getLong(_cursorIndexOfChatId);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpData;
              if (_cursor.isNull(_cursorIndexOfData)) {
                _tmpData = null;
              } else {
                _tmpData = _cursor.getString(_cursorIndexOfData);
              }
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
              final int _tmpType_message;
              _tmpType_message = _cursor.getInt(_cursorIndexOfTypeMessage);
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
              final boolean _tmpReaded;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfReaded);
              _tmpReaded = _tmp_3 != 0;
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final boolean _tmpIs_user;
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsUser);
              _tmpIs_user = _tmp_4 != 0;
              _tmpMessage = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
              final Chat _tmpChat;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfChatId);
              _tmpChat = _collectionChat.get(_tmpKey_1);
              _item = new MessageWithChat();
              _item.message = _tmpMessage;
              _item.setChat(_tmpChat);
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
    }, continuation);
  }

  @Override
  public Object getLastMessageSended(final long chatId,
      final Continuation<? super Message> continuation) {
    final String _sql = "select * from messages where sended = 1 and chat_id = ? order by created_at desc limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chatId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Message>() {
      @Override
      @Nullable
      public Message call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChatId = CursorUtil.getColumnIndexOrThrow(_cursor, "chat_id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfTypeMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "type_message");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
          final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
          final int _cursorIndexOfReaded = CursorUtil.getColumnIndexOrThrow(_cursor, "readed");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfIsUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_user");
          final Message _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpChat_id;
            _tmpChat_id = _cursor.getLong(_cursorIndexOfChatId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpData;
            if (_cursor.isNull(_cursorIndexOfData)) {
              _tmpData = null;
            } else {
              _tmpData = _cursor.getString(_cursorIndexOfData);
            }
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
            final int _tmpType_message;
            _tmpType_message = _cursor.getInt(_cursorIndexOfTypeMessage);
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
            final boolean _tmpReaded;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfReaded);
            _tmpReaded = _tmp_3 != 0;
            final long _tmpParent_id;
            _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
            final boolean _tmpIs_user;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsUser);
            _tmpIs_user = _tmp_4 != 0;
            _result = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
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

  private void __fetchRelationshipmessagesAsappRegateModelsMessage(
      @NonNull final LongSparseArray<Message> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipmessagesAsappRegateModelsMessage(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`chat_id`,`content`,`data`,`created_at`,`type_message`,`profile_id`,`reply_to`,`sended`,`readed`,`parent_id`,`is_user` FROM `messages` WHERE `id` IN (");
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
      final int _cursorIndexOfChatId = 1;
      final int _cursorIndexOfContent = 2;
      final int _cursorIndexOfData = 3;
      final int _cursorIndexOfCreatedAt = 4;
      final int _cursorIndexOfTypeMessage = 5;
      final int _cursorIndexOfProfileId = 6;
      final int _cursorIndexOfReplyTo = 7;
      final int _cursorIndexOfSended = 8;
      final int _cursorIndexOfReaded = 9;
      final int _cursorIndexOfParentId = 10;
      final int _cursorIndexOfIsUser = 11;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Message _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpChat_id;
          _tmpChat_id = _cursor.getLong(_cursorIndexOfChatId);
          final String _tmpContent;
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
          final String _tmpData;
          if (_cursor.isNull(_cursorIndexOfData)) {
            _tmpData = null;
          } else {
            _tmpData = _cursor.getString(_cursorIndexOfData);
          }
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
          final int _tmpType_message;
          _tmpType_message = _cursor.getInt(_cursorIndexOfTypeMessage);
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
          final boolean _tmpReaded;
          final int _tmp_3;
          _tmp_3 = _cursor.getInt(_cursorIndexOfReaded);
          _tmpReaded = _tmp_3 != 0;
          final long _tmpParent_id;
          _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
          final boolean _tmpIs_user;
          final int _tmp_4;
          _tmp_4 = _cursor.getInt(_cursorIndexOfIsUser);
          _tmpIs_user = _tmp_4 != 0;
          _item_1 = new Message(_tmpId,_tmpChat_id,_tmpContent,_tmpData,_tmpCreated_at,_tmpType_message,_tmpProfile_id,_tmpReply_to,_tmpSended,_tmpReaded,_tmpParent_id,_tmpIs_user);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipchatAsappRegateModelsChatChat(
      @NonNull final LongSparseArray<Chat> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipchatAsappRegateModelsChatChat(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`photo`,`name`,`last_message`,`last_message_created`,`messages_count`,`type_chat`,`parent_id`,`updated_at` FROM `chat` WHERE `id` IN (");
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
      final int _cursorIndexOfPhoto = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfLastMessage = 3;
      final int _cursorIndexOfLastMessageCreated = 4;
      final int _cursorIndexOfMessagesCount = 5;
      final int _cursorIndexOfTypeChat = 6;
      final int _cursorIndexOfParentId = 7;
      final int _cursorIndexOfUpdatedAt = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Chat _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpPhoto;
          if (_cursor.isNull(_cursorIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _cursor.getString(_cursorIndexOfPhoto);
          }
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpLast_message;
          if (_cursor.isNull(_cursorIndexOfLastMessage)) {
            _tmpLast_message = null;
          } else {
            _tmpLast_message = _cursor.getString(_cursorIndexOfLastMessage);
          }
          final Instant _tmpLast_message_created;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfLastMessageCreated)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfLastMessageCreated);
          }
          _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          final int _tmpMessages_count;
          _tmpMessages_count = _cursor.getInt(_cursorIndexOfMessagesCount);
          final int _tmpType_chat;
          _tmpType_chat = _cursor.getInt(_cursorIndexOfTypeChat);
          final long _tmpParent_id;
          _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
          final Instant _tmpUpdated_at;
          final String _tmp_1;
          if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _cursor.getString(_cursorIndexOfUpdatedAt);
          }
          final Instant _tmp_2 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_1);
          if (_tmp_2 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpUpdated_at = _tmp_2;
          }
          _item_1 = new Chat(_tmpId,_tmpPhoto,_tmpName,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count,_tmpType_chat,_tmpParent_id,_tmpUpdated_at);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
