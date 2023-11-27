package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.LastUpdatedEntity;
import app.regate.models.UpdatedEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.datetime.Instant;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomLastUpdatedEntityDao_Impl extends RoomLastUpdatedEntityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LastUpdatedEntity> __insertionAdapterOfLastUpdatedEntity;

  private final EntityDeletionOrUpdateAdapter<LastUpdatedEntity> __deletionAdapterOfLastUpdatedEntity;

  private final EntityDeletionOrUpdateAdapter<LastUpdatedEntity> __updateAdapterOfLastUpdatedEntity;

  private final EntityUpsertionAdapter<LastUpdatedEntity> __upsertionAdapterOfLastUpdatedEntity;

  public RoomLastUpdatedEntityDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLastUpdatedEntity = new EntityInsertionAdapter<LastUpdatedEntity>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `last_updated_entity` (`entity_id`,`created_at`) VALUES (?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LastUpdatedEntity entity) {
        statement.bindString(1, __UpdatedEntity_enumToString(entity.getEntity_id()));
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
      }
    };
    this.__deletionAdapterOfLastUpdatedEntity = new EntityDeletionOrUpdateAdapter<LastUpdatedEntity>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `last_updated_entity` WHERE `entity_id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LastUpdatedEntity entity) {
        statement.bindString(1, __UpdatedEntity_enumToString(entity.getEntity_id()));
      }
    };
    this.__updateAdapterOfLastUpdatedEntity = new EntityDeletionOrUpdateAdapter<LastUpdatedEntity>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `last_updated_entity` SET `entity_id` = ?,`created_at` = ? WHERE `entity_id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LastUpdatedEntity entity) {
        statement.bindString(1, __UpdatedEntity_enumToString(entity.getEntity_id()));
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __UpdatedEntity_enumToString(entity.getEntity_id()));
      }
    };
    this.__upsertionAdapterOfLastUpdatedEntity = new EntityUpsertionAdapter<LastUpdatedEntity>(new EntityInsertionAdapter<LastUpdatedEntity>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `last_updated_entity` (`entity_id`,`created_at`) VALUES (?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LastUpdatedEntity entity) {
        statement.bindString(1, __UpdatedEntity_enumToString(entity.getEntity_id()));
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<LastUpdatedEntity>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `last_updated_entity` SET `entity_id` = ?,`created_at` = ? WHERE `entity_id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LastUpdatedEntity entity) {
        statement.bindString(1, __UpdatedEntity_enumToString(entity.getEntity_id()));
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __UpdatedEntity_enumToString(entity.getEntity_id()));
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final LastUpdatedEntity entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLastUpdatedEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends LastUpdatedEntity> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLastUpdatedEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final LastUpdatedEntity entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfLastUpdatedEntity.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final LastUpdatedEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLastUpdatedEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final LastUpdatedEntity entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfLastUpdatedEntity.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final LastUpdatedEntity[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfLastUpdatedEntity.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends LastUpdatedEntity> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfLastUpdatedEntity.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getLastUpdatedEntity(final UpdatedEntity entity,
      final Continuation<? super LastUpdatedEntity> continuation) {
    final String _sql = "select * from last_updated_entity where entity_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __UpdatedEntity_enumToString(entity));
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LastUpdatedEntity>() {
      @Override
      @Nullable
      public LastUpdatedEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entity_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final LastUpdatedEntity _result;
          if (_cursor.moveToFirst()) {
            final UpdatedEntity _tmpEntity_id;
            _tmpEntity_id = __UpdatedEntity_stringToEnum(_cursor.getString(_cursorIndexOfEntityId));
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
            _result = new LastUpdatedEntity(_tmpEntity_id,_tmpCreated_at);
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

  private String __UpdatedEntity_enumToString(@NonNull final UpdatedEntity _value) {
    switch (_value) {
      case NOTIFICATIONS: return "NOTIFICATIONS";
      case RESERVAS: return "RESERVAS";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private UpdatedEntity __UpdatedEntity_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "NOTIFICATIONS": return UpdatedEntity.NOTIFICATIONS;
      case "RESERVAS": return UpdatedEntity.RESERVAS;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
