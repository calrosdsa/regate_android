package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
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
import app.regate.models.MessageInbox;
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
import kotlinx.datetime.Instant;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomMessageInboxDao_Impl extends RoomMessageInboxDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<MessageInbox> __deletionAdapterOfMessageInbox;

  private final EntityDeletionOrUpdateAdapter<MessageInbox> __updateAdapterOfMessageInbox;

  private final EntityUpsertionAdapter<MessageInbox> __upsertionAdapterOfMessageInbox;

  public RoomMessageInboxDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfMessageInbox = new EntityDeletionOrUpdateAdapter<MessageInbox>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `message_inbox` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageInbox entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMessageInbox = new EntityDeletionOrUpdateAdapter<MessageInbox>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `message_inbox` SET `id` = ?,`conversation_id` = ?,`content` = ?,`created_at` = ?,`sender_id` = ?,`reply_to` = ?,`sended` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageInbox entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getConversation_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getSender_id());
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
    this.__upsertionAdapterOfMessageInbox = new EntityUpsertionAdapter<MessageInbox>(new EntityInsertionAdapter<MessageInbox>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `message_inbox` (`id`,`conversation_id`,`content`,`created_at`,`sender_id`,`reply_to`,`sended`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageInbox entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getConversation_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getSender_id());
        if (entity.getReply_to() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReply_to());
        }
        final int _tmp_1 = entity.getSended() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
      }
    }, new EntityDeletionOrUpdateAdapter<MessageInbox>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `message_inbox` SET `id` = ?,`conversation_id` = ?,`content` = ?,`created_at` = ?,`sender_id` = ?,`reply_to` = ?,`sended` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageInbox entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getConversation_id());
        statement.bindString(3, entity.getContent());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getSender_id());
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
  public Object deleteEntity(final MessageInbox entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfMessageInbox.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final MessageInbox entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageInbox.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final MessageInbox entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfMessageInbox.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final MessageInbox[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessageInbox.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends MessageInbox> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfMessageInbox.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public PagingSource<Integer, MessageInbox> observeMessages(final long id) {
    final String _sql = "SELECT * FROM message_inbox where conversation_id = ? ORDER BY datetime(created_at) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return new LimitOffsetPagingSource<MessageInbox>(_statement, __db, "message_inbox") {
      @Override
      @NonNull
      protected List<MessageInbox> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(cursor, "conversation_id");
        final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(cursor, "content");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "created_at");
        final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(cursor, "sender_id");
        final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(cursor, "reply_to");
        final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(cursor, "sended");
        final List<MessageInbox> _result = new ArrayList<MessageInbox>(cursor.getCount());
        while (cursor.moveToNext()) {
          final MessageInbox _item;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final long _tmpConversation_id;
          _tmpConversation_id = cursor.getLong(_cursorIndexOfConversationId);
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
          final long _tmpSender_id;
          _tmpSender_id = cursor.getLong(_cursorIndexOfSenderId);
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
          _item = new MessageInbox(_tmpId,_tmpConversation_id,_tmpContent,_tmpCreated_at,_tmpSender_id,_tmpReply_to,_tmpSended);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Object getUnSendedMessage(final long sender, final long conversationId,
      final Continuation<? super List<MessageInbox>> continuation) {
    final String _sql = "select *  from message_inbox where sender_id = ? and sended = 0 and conversation_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sender);
    _argIndex = 2;
    _statement.bindLong(_argIndex, conversationId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageInbox>>() {
      @Override
      @NonNull
      public List<MessageInbox> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversation_id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "sender_id");
          final int _cursorIndexOfReplyTo = CursorUtil.getColumnIndexOrThrow(_cursor, "reply_to");
          final int _cursorIndexOfSended = CursorUtil.getColumnIndexOrThrow(_cursor, "sended");
          final List<MessageInbox> _result = new ArrayList<MessageInbox>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageInbox _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpConversation_id;
            _tmpConversation_id = _cursor.getLong(_cursorIndexOfConversationId);
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
            final long _tmpSender_id;
            _tmpSender_id = _cursor.getLong(_cursorIndexOfSenderId);
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
            _item = new MessageInbox(_tmpId,_tmpConversation_id,_tmpContent,_tmpCreated_at,_tmpSender_id,_tmpReply_to,_tmpSended);
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
}
