package app.regate.data.daos;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.InstalacionCupos;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Cupo;
import app.regate.models.Instalacion;
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
public final class RoomCupoDao_Impl extends RoomCupoDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<Cupo> __deletionAdapterOfCupo;

  private final EntityDeletionOrUpdateAdapter<Cupo> __updateAdapterOfCupo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCupos;

  private final EntityUpsertionAdapter<Cupo> __upsertionAdapterOfCupo;

  public RoomCupoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfCupo = new EntityDeletionOrUpdateAdapter<Cupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `cupos` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cupo entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCupo = new EntityDeletionOrUpdateAdapter<Cupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `cupos` SET `id` = ?,`time` = ?,`instalacion_id` = ?,`price` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cupo entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindLong(3, entity.getInstalacion_id());
        statement.bindDouble(4, entity.getPrice());
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCupos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cupos";
        return _query;
      }
    };
    this.__upsertionAdapterOfCupo = new EntityUpsertionAdapter<Cupo>(new EntityInsertionAdapter<Cupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `cupos` (`id`,`time`,`instalacion_id`,`price`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cupo entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindLong(3, entity.getInstalacion_id());
        statement.bindDouble(4, entity.getPrice());
      }
    }, new EntityDeletionOrUpdateAdapter<Cupo>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `cupos` SET `id` = ?,`time` = ?,`instalacion_id` = ?,`price` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cupo entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindLong(3, entity.getInstalacion_id());
        statement.bindDouble(4, entity.getPrice());
        statement.bindLong(5, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final Cupo entity, final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfCupo.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Cupo entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCupo.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteCupos(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCupos.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteCupos.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object upsert(final Cupo entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfCupo.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Cupo[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfCupo.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Cupo> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfCupo.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Cupo>> observeCupos(final long id) {
    final String _sql = "SELECT * FROM cupos WHERE instalacion_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"cupos"}, new Callable<List<Cupo>>() {
      @Override
      @NonNull
      public List<Cupo> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
            final int _cursorIndexOfInstalacionId = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_id");
            final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
            final List<Cupo> _result = new ArrayList<Cupo>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Cupo _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Instant _tmpTime;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfTime)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfTime);
              }
              final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpTime = _tmp_1;
              }
              final long _tmpInstalacion_id;
              _tmpInstalacion_id = _cursor.getLong(_cursorIndexOfInstalacionId);
              final double _tmpPrice;
              _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
              _item = new Cupo(_tmpId,_tmpTime,_tmpInstalacion_id,_tmpPrice);
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
  public Flow<Cupo> observeLastCupo() {
    final String _sql = "SELECT * FROM cupos limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"cupos"}, new Callable<Cupo>() {
      @Override
      @Nullable
      public Cupo call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
            final int _cursorIndexOfInstalacionId = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_id");
            final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
            final Cupo _result;
            if (_cursor.moveToFirst()) {
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Instant _tmpTime;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfTime)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfTime);
              }
              final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpTime = _tmp_1;
              }
              final long _tmpInstalacion_id;
              _tmpInstalacion_id = _cursor.getLong(_cursorIndexOfInstalacionId);
              final double _tmpPrice;
              _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
              _result = new Cupo(_tmpId,_tmpTime,_tmpInstalacion_id,_tmpPrice);
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
  public Object getInstalacionCupos(final long id,
      final Continuation<? super InstalacionCupos> continuation) {
    final String _sql = "SELECT * FROM instalaciones where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<InstalacionCupos>() {
      @Override
      @NonNull
      public InstalacionCupos call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfCantidadPersonas = CursorUtil.getColumnIndexOrThrow(_cursor, "cantidad_personas");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
            final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "category_name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfEstablecimientoId = CursorUtil.getColumnIndexOrThrow(_cursor, "establecimiento_id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfPrecioHora = CursorUtil.getColumnIndexOrThrow(_cursor, "precio_hora");
            final int _cursorIndexOfPortada = CursorUtil.getColumnIndexOrThrow(_cursor, "portada");
            final LongSparseArray<ArrayList<Cupo>> _collectionCupos = new LongSparseArray<ArrayList<Cupo>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionCupos.containsKey(_tmpKey)) {
                _collectionCupos.put(_tmpKey, new ArrayList<Cupo>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcuposAsappRegateModelsCupo(_collectionCupos);
            final InstalacionCupos _result;
            if (_cursor.moveToFirst()) {
              final Instalacion _tmpInstalacion;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Integer _tmpCantidad_personas;
              if (_cursor.isNull(_cursorIndexOfCantidadPersonas)) {
                _tmpCantidad_personas = null;
              } else {
                _tmpCantidad_personas = _cursor.getInt(_cursorIndexOfCantidadPersonas);
              }
              final Integer _tmpCategory_id;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategory_id = null;
              } else {
                _tmpCategory_id = _cursor.getInt(_cursorIndexOfCategoryId);
              }
              final String _tmpCategory_name;
              if (_cursor.isNull(_cursorIndexOfCategoryName)) {
                _tmpCategory_name = null;
              } else {
                _tmpCategory_name = _cursor.getString(_cursorIndexOfCategoryName);
              }
              final String _tmpDescription;
              if (_cursor.isNull(_cursorIndexOfDescription)) {
                _tmpDescription = null;
              } else {
                _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              }
              final long _tmpEstablecimiento_id;
              _tmpEstablecimiento_id = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final Integer _tmpPrecio_hora;
              if (_cursor.isNull(_cursorIndexOfPrecioHora)) {
                _tmpPrecio_hora = null;
              } else {
                _tmpPrecio_hora = _cursor.getInt(_cursorIndexOfPrecioHora);
              }
              final String _tmpPortada;
              if (_cursor.isNull(_cursorIndexOfPortada)) {
                _tmpPortada = null;
              } else {
                _tmpPortada = _cursor.getString(_cursorIndexOfPortada);
              }
              _tmpInstalacion = new Instalacion(_tmpId,_tmpCantidad_personas,_tmpCategory_id,_tmpCategory_name,_tmpDescription,_tmpEstablecimiento_id,_tmpName,_tmpPrecio_hora,_tmpPortada);
              final ArrayList<Cupo> _tmpCuposCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpCuposCollection = _collectionCupos.get(_tmpKey_1);
              _result = new InstalacionCupos();
              _result.instalacion = _tmpInstalacion;
              _result.cupos = _tmpCuposCollection;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcuposAsappRegateModelsCupo(
      @NonNull final LongSparseArray<ArrayList<Cupo>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipcuposAsappRegateModelsCupo(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`time`,`instalacion_id`,`price` FROM `cupos` WHERE `instalacion_id` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "instalacion_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTime = 1;
      final int _cursorIndexOfInstalacionId = 2;
      final int _cursorIndexOfPrice = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Cupo> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Cupo _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final Instant _tmpTime;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfTime)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfTime);
          }
          final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
          } else {
            _tmpTime = _tmp_1;
          }
          final long _tmpInstalacion_id;
          _tmpInstalacion_id = _cursor.getLong(_cursorIndexOfInstalacionId);
          final double _tmpPrice;
          _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
          _item_1 = new Cupo(_tmpId,_tmpTime,_tmpInstalacion_id,_tmpPrice);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
