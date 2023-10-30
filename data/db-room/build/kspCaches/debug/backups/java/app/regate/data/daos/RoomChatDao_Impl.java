package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.chat.Chat;
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
public final class RoomChatDao_Impl extends RoomChatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Chat> __insertionAdapterOfChat;

  private final EntityDeletionOrUpdateAdapter<Chat> __deletionAdapterOfChat;

  private final EntityDeletionOrUpdateAdapter<Chat> __updateAdapterOfChat;

  private final EntityUpsertionAdapter<Chat> __upsertionAdapterOfChat;

  public RoomChatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChat = new EntityInsertionAdapter<Chat>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `chat` (`id`,`photo`,`name`,`last_message`,`last_message_created`,`messages_count`,`type_chat`,`is_message_deleted`,`parent_id`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPhoto() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPhoto());
        }
        statement.bindString(3, entity.getName());
        if (entity.getLast_message() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLast_message());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getMessages_count());
        statement.bindLong(7, entity.getType_chat());
        final int _tmp_1 = entity.is_message_deleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getParent_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getUpdated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfChat = new EntityDeletionOrUpdateAdapter<Chat>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `chat` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfChat = new EntityDeletionOrUpdateAdapter<Chat>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `chat` SET `id` = ?,`photo` = ?,`name` = ?,`last_message` = ?,`last_message_created` = ?,`messages_count` = ?,`type_chat` = ?,`is_message_deleted` = ?,`parent_id` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPhoto() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPhoto());
        }
        statement.bindString(3, entity.getName());
        if (entity.getLast_message() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLast_message());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getMessages_count());
        statement.bindLong(7, entity.getType_chat());
        final int _tmp_1 = entity.is_message_deleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getParent_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getUpdated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__upsertionAdapterOfChat = new EntityUpsertionAdapter<Chat>(new EntityInsertionAdapter<Chat>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `chat` (`id`,`photo`,`name`,`last_message`,`last_message_created`,`messages_count`,`type_chat`,`is_message_deleted`,`parent_id`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPhoto() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPhoto());
        }
        statement.bindString(3, entity.getName());
        if (entity.getLast_message() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLast_message());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getMessages_count());
        statement.bindLong(7, entity.getType_chat());
        final int _tmp_1 = entity.is_message_deleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getParent_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getUpdated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Chat>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `chat` SET `id` = ?,`photo` = ?,`name` = ?,`last_message` = ?,`last_message_created` = ?,`messages_count` = ?,`type_chat` = ?,`is_message_deleted` = ?,`parent_id` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chat entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPhoto() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPhoto());
        }
        statement.bindString(3, entity.getName());
        if (entity.getLast_message() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLast_message());
        }
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getLast_message_created());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        statement.bindLong(6, entity.getMessages_count());
        statement.bindLong(7, entity.getType_chat());
        final int _tmp_1 = entity.is_message_deleted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getParent_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getUpdated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        statement.bindLong(11, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Chat entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChat.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Chat> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChat.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Chat entity, final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfChat.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Chat entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChat.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final Chat entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfChat.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Chat[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfChat.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Chat> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfChat.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public PagingSource<Integer, Chat> observeChatsPaging() {
    final String _sql = "\n"
            + "               SELECT c.id,c.name,c.photo,\n"
            + "    (select content from messages where chat_id = c.id order by created_at DESC limit 1) as last_message,\n"
            + "    (select created_at from messages where chat_id = c.id order by created_at DESC limit 1) as last_message_created,\n"
            + "    (select count(*) from messages where chat_id = c.id and readed = 0) as messages_count,\n"
            + "    (select is_deleted from messages where chat_id = c.id order by created_at DESC limit 1) as is_message_deleted,\n"
            + "    type_chat,parent_id,updated_at\n"
            + "    FROM chat as c order by last_message_created desc\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new LimitOffsetPagingSource<Chat>(_statement, __db, "messages", "chat") {
      @Override
      @NonNull
      protected List<Chat> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = 0;
        final int _cursorIndexOfName = 1;
        final int _cursorIndexOfPhoto = 2;
        final int _cursorIndexOfLastMessage = 3;
        final int _cursorIndexOfLastMessageCreated = 4;
        final int _cursorIndexOfMessagesCount = 5;
        final int _cursorIndexOfIsMessageDeleted = 6;
        final int _cursorIndexOfTypeChat = 7;
        final int _cursorIndexOfParentId = 8;
        final int _cursorIndexOfUpdatedAt = 9;
        final List<Chat> _result = new ArrayList<Chat>(cursor.getCount());
        while (cursor.moveToNext()) {
          final Chat _item;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = cursor.getString(_cursorIndexOfName);
          final String _tmpPhoto;
          if (cursor.isNull(_cursorIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = cursor.getString(_cursorIndexOfPhoto);
          }
          final String _tmpLast_message;
          if (cursor.isNull(_cursorIndexOfLastMessage)) {
            _tmpLast_message = null;
          } else {
            _tmpLast_message = cursor.getString(_cursorIndexOfLastMessage);
          }
          final Instant _tmpLast_message_created;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfLastMessageCreated)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfLastMessageCreated);
          }
          _tmpLast_message_created = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          final int _tmpMessages_count;
          _tmpMessages_count = cursor.getInt(_cursorIndexOfMessagesCount);
          final boolean _tmpIs_message_deleted;
          final int _tmp_1;
          _tmp_1 = cursor.getInt(_cursorIndexOfIsMessageDeleted);
          _tmpIs_message_deleted = _tmp_1 != 0;
          final int _tmpType_chat;
          _tmpType_chat = cursor.getInt(_cursorIndexOfTypeChat);
          final long _tmpParent_id;
          _tmpParent_id = cursor.getLong(_cursorIndexOfParentId);
          final Instant _tmpUpdated_at;
          final String _tmp_2;
          if (cursor.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = cursor.getString(_cursorIndexOfUpdatedAt);
          }
          final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
          if (_tmp_3 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpUpdated_at = _tmp_3;
          }
          _item = new Chat(_tmpId,_tmpPhoto,_tmpName,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count,_tmpType_chat,_tmpIs_message_deleted,_tmpParent_id,_tmpUpdated_at);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Flow<Chat> observeChat(final long id) {
    final String _sql = "select * from chat where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"chat"}, new Callable<Chat>() {
      @Override
      @NonNull
      public Chat call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
            final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
            final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
            final int _cursorIndexOfTypeChat = CursorUtil.getColumnIndexOrThrow(_cursor, "type_chat");
            final int _cursorIndexOfIsMessageDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_message_deleted");
            final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
            final Chat _result;
            if (_cursor.moveToFirst()) {
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
              final boolean _tmpIs_message_deleted;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsMessageDeleted);
              _tmpIs_message_deleted = _tmp_1 != 0;
              final long _tmpParent_id;
              _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
              final Instant _tmpUpdated_at;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfUpdatedAt);
              }
              final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpUpdated_at = _tmp_3;
              }
              _result = new Chat(_tmpId,_tmpPhoto,_tmpName,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count,_tmpType_chat,_tmpIs_message_deleted,_tmpParent_id,_tmpUpdated_at);
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
  public Object getChat(final long id, final Continuation<? super Chat> continuation) {
    final String _sql = "select * from chat where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Chat>() {
      @Override
      @Nullable
      public Chat call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message");
          final int _cursorIndexOfLastMessageCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_message_created");
          final int _cursorIndexOfMessagesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "messages_count");
          final int _cursorIndexOfTypeChat = CursorUtil.getColumnIndexOrThrow(_cursor, "type_chat");
          final int _cursorIndexOfIsMessageDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_message_deleted");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_id");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final Chat _result;
          if (_cursor.moveToFirst()) {
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
            final boolean _tmpIs_message_deleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMessageDeleted);
            _tmpIs_message_deleted = _tmp_1 != 0;
            final long _tmpParent_id;
            _tmpParent_id = _cursor.getLong(_cursorIndexOfParentId);
            final Instant _tmpUpdated_at;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
            } else {
              _tmpUpdated_at = _tmp_3;
            }
            _result = new Chat(_tmpId,_tmpPhoto,_tmpName,_tmpLast_message,_tmpLast_message_created,_tmpMessages_count,_tmpType_chat,_tmpIs_message_deleted,_tmpParent_id,_tmpUpdated_at);
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
