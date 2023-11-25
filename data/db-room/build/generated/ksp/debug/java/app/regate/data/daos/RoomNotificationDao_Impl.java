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
import app.regate.data.db.AppTypeConverters;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Notification;
import app.regate.models.TypeEntity;
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
public final class RoomNotificationDao_Impl extends RoomNotificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Notification> __insertionAdapterOfNotification;

  private final EntityDeletionOrUpdateAdapter<Notification> __deletionAdapterOfNotification;

  private final EntityDeletionOrUpdateAdapter<Notification> __updateAdapterOfNotification;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUnreadNotifications;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<Notification> __upsertionAdapterOfNotification;

  public RoomNotificationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNotification = new EntityInsertionAdapter<Notification>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `notification` (`id`,`title`,`content`,`entityId`,`typeEntity`,`read`,`image`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Notification entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        if (entity.getEntityId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEntityId());
        }
        final Integer _tmp;
        if (entity.getTypeEntity() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromTypeEntity(entity.getTypeEntity());
        }
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.getRead() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getImage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImage());
        }
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfNotification = new EntityDeletionOrUpdateAdapter<Notification>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `notification` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Notification entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNotification = new EntityDeletionOrUpdateAdapter<Notification>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `notification` SET `id` = ?,`title` = ?,`content` = ?,`entityId` = ?,`typeEntity` = ?,`read` = ?,`image` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Notification entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        if (entity.getEntityId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEntityId());
        }
        final Integer _tmp;
        if (entity.getTypeEntity() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromTypeEntity(entity.getTypeEntity());
        }
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.getRead() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getImage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImage());
        }
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateUnreadNotifications = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "update notification set read = 1";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from notification";
        return _query;
      }
    };
    this.__upsertionAdapterOfNotification = new EntityUpsertionAdapter<Notification>(new EntityInsertionAdapter<Notification>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `notification` (`id`,`title`,`content`,`entityId`,`typeEntity`,`read`,`image`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Notification entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        if (entity.getEntityId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEntityId());
        }
        final Integer _tmp;
        if (entity.getTypeEntity() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromTypeEntity(entity.getTypeEntity());
        }
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.getRead() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getImage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImage());
        }
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Notification>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `notification` SET `id` = ?,`title` = ?,`content` = ?,`entityId` = ?,`typeEntity` = ?,`read` = ?,`image` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Notification entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        statement.bindString(3, entity.getContent());
        if (entity.getEntityId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEntityId());
        }
        final Integer _tmp;
        if (entity.getTypeEntity() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromTypeEntity(entity.getTypeEntity());
        }
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.getRead() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getImage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImage());
        }
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
        statement.bindLong(9, entity.getId());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Notification entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNotification.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Notification> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNotification.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Notification entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfNotification.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Notification entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNotification.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateUnreadNotifications(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUnreadNotifications.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateUnreadNotifications.release(_stmt);
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
  public Object upsert(final Notification entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfNotification.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Notification[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfNotification.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Notification> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfNotification.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Notification>> getNotificaciones() {
    final String _sql = "select * from notification order by created_at desc limit 500";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"notification"}, new Callable<List<Notification>>() {
      @Override
      @NonNull
      public List<Notification> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entityId");
            final int _cursorIndexOfTypeEntity = CursorUtil.getColumnIndexOrThrow(_cursor, "typeEntity");
            final int _cursorIndexOfRead = CursorUtil.getColumnIndexOrThrow(_cursor, "read");
            final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final List<Notification> _result = new ArrayList<Notification>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Notification _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              if (_cursor.isNull(_cursorIndexOfTitle)) {
                _tmpTitle = null;
              } else {
                _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              }
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final Long _tmpEntityId;
              if (_cursor.isNull(_cursorIndexOfEntityId)) {
                _tmpEntityId = null;
              } else {
                _tmpEntityId = _cursor.getLong(_cursorIndexOfEntityId);
              }
              final TypeEntity _tmpTypeEntity;
              final Integer _tmp;
              if (_cursor.isNull(_cursorIndexOfTypeEntity)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getInt(_cursorIndexOfTypeEntity);
              }
              _tmpTypeEntity = AppTypeConverters.INSTANCE.toTypeEntity(_tmp);
              final boolean _tmpRead;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfRead);
              _tmpRead = _tmp_1 != 0;
              final String _tmpImage;
              if (_cursor.isNull(_cursorIndexOfImage)) {
                _tmpImage = null;
              } else {
                _tmpImage = _cursor.getString(_cursorIndexOfImage);
              }
              final Instant _tmpCreated_at;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpCreated_at = _tmp_3;
              }
              _item = new Notification(_tmpId,_tmpTitle,_tmpContent,_tmpEntityId,_tmpTypeEntity,_tmpRead,_tmpImage,_tmpCreated_at);
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
  public Flow<Integer> observeUnReadNotificationsCount() {
    final String _sql = "select count(*) from notification where read = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"notification"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final Integer _result;
            if (_cursor.moveToFirst()) {
              final int _tmp;
              _tmp = _cursor.getInt(0);
              _result = _tmp;
            } else {
              _result = 0;
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
