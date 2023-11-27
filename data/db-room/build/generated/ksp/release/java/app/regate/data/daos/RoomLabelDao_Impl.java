package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.data.db.AppTypeConverters;
import app.regate.models.LabelType;
import app.regate.models.Labels;
import java.lang.Class;
import java.lang.Exception;
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
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomLabelDao_Impl extends RoomLabelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Labels> __insertionAdapterOfLabels;

  private final EntityDeletionOrUpdateAdapter<Labels> __deletionAdapterOfLabels;

  private final EntityDeletionOrUpdateAdapter<Labels> __updateAdapterOfLabels;

  private final EntityUpsertionAdapter<Labels> __upsertionAdapterOfLabels;

  public RoomLabelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLabels = new EntityInsertionAdapter<Labels>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR IGNORE INTO `labels` (`id`,`name`,`thumbnail`,`type_label`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Labels entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getThumbnail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getThumbnail());
        }
        final String _tmp;
        if (entity.getType_label() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromLabelType(entity.getType_label());
        }
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    };
    this.__deletionAdapterOfLabels = new EntityDeletionOrUpdateAdapter<Labels>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `labels` WHERE `id` = ? AND `name` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Labels entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__updateAdapterOfLabels = new EntityDeletionOrUpdateAdapter<Labels>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `labels` SET `id` = ?,`name` = ?,`thumbnail` = ?,`type_label` = ? WHERE `id` = ? AND `name` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Labels entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getThumbnail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getThumbnail());
        }
        final String _tmp;
        if (entity.getType_label() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromLabelType(entity.getType_label());
        }
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getId());
        statement.bindString(6, entity.getName());
      }
    };
    this.__upsertionAdapterOfLabels = new EntityUpsertionAdapter<Labels>(new EntityInsertionAdapter<Labels>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `labels` (`id`,`name`,`thumbnail`,`type_label`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Labels entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getThumbnail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getThumbnail());
        }
        final String _tmp;
        if (entity.getType_label() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromLabelType(entity.getType_label());
        }
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Labels>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `labels` SET `id` = ?,`name` = ?,`thumbnail` = ?,`type_label` = ? WHERE `id` = ? AND `name` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Labels entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getThumbnail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getThumbnail());
        }
        final String _tmp;
        if (entity.getType_label() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromLabelType(entity.getType_label());
        }
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getId());
        statement.bindString(6, entity.getName());
      }
    });
  }

  @Override
  public Object insertOnConflictIgnore(final Labels entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLabels.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllonConflictIgnore(final List<? extends Labels> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLabels.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Labels entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfLabels.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Labels entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLabels.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final Labels entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfLabels.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Labels[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfLabels.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Labels> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfLabels.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Labels>> observeLabelByType(final LabelType type) {
    final String _sql = "select * from labels where type_label = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = AppTypeConverters.INSTANCE.fromLabelType(type);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"labels"}, new Callable<List<Labels>>() {
      @Override
      @NonNull
      public List<Labels> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfThumbnail = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail");
            final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
            final List<Labels> _result = new ArrayList<Labels>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Labels _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpThumbnail;
              if (_cursor.isNull(_cursorIndexOfThumbnail)) {
                _tmpThumbnail = null;
              } else {
                _tmpThumbnail = _cursor.getString(_cursorIndexOfThumbnail);
              }
              final LabelType _tmpType_label;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfTypeLabel)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfTypeLabel);
              }
              _tmpType_label = AppTypeConverters.INSTANCE.toLabelType(_tmp_1);
              _item = new Labels(_tmpId,_tmpName,_tmpThumbnail,_tmpType_label);
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
  public Object getLabelsByType(final LabelType type,
      final Continuation<? super List<Labels>> continuation) {
    final String _sql = "select * from labels where type_label = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = AppTypeConverters.INSTANCE.fromLabelType(type);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<Labels>>() {
      @Override
      @NonNull
      public List<Labels> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfThumbnail = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail");
            final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
            final List<Labels> _result = new ArrayList<Labels>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Labels _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpThumbnail;
              if (_cursor.isNull(_cursorIndexOfThumbnail)) {
                _tmpThumbnail = null;
              } else {
                _tmpThumbnail = _cursor.getString(_cursorIndexOfThumbnail);
              }
              final LabelType _tmpType_label;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfTypeLabel)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfTypeLabel);
              }
              _tmpType_label = AppTypeConverters.INSTANCE.toLabelType(_tmp_1);
              _item = new Labels(_tmpId,_tmpName,_tmpThumbnail,_tmpType_label);
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
  public Flow<List<Labels>> observeLabelByIdsAndType(final LabelType type, final List<Long> ids) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("select * from labels where type_label = ");
    _stringBuilder.append("?");
    _stringBuilder.append(" and id in (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    final String _tmp = AppTypeConverters.INSTANCE.fromLabelType(type);
    _statement.bindString(_argIndex, _tmp);
    _argIndex = 2;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, true, new String[] {"labels"}, new Callable<List<Labels>>() {
      @Override
      @NonNull
      public List<Labels> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfThumbnail = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail");
            final int _cursorIndexOfTypeLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "type_label");
            final List<Labels> _result = new ArrayList<Labels>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Labels _item_1;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpThumbnail;
              if (_cursor.isNull(_cursorIndexOfThumbnail)) {
                _tmpThumbnail = null;
              } else {
                _tmpThumbnail = _cursor.getString(_cursorIndexOfThumbnail);
              }
              final LabelType _tmpType_label;
              final String _tmp_1;
              if (_cursor.isNull(_cursorIndexOfTypeLabel)) {
                _tmp_1 = null;
              } else {
                _tmp_1 = _cursor.getString(_cursorIndexOfTypeLabel);
              }
              _tmpType_label = AppTypeConverters.INSTANCE.toLabelType(_tmp_1);
              _item_1 = new Labels(_tmpId,_tmpName,_tmpThumbnail,_tmpType_label);
              _result.add(_item_1);
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
