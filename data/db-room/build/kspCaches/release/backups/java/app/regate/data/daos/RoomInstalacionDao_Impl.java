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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.compoundmodels.InstalacionCategoryCount;
import app.regate.data.db.AppTypeConverters;
import app.regate.models.Instalacion;
import app.regate.models.LabelType;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomInstalacionDao_Impl extends RoomInstalacionDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<Instalacion> __deletionAdapterOfInstalacion;

  private final EntityDeletionOrUpdateAdapter<Instalacion> __updateAdapterOfInstalacion;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<Instalacion> __upsertionAdapterOfInstalacion;

  public RoomInstalacionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfInstalacion = new EntityDeletionOrUpdateAdapter<Instalacion>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `instalaciones` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Instalacion entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfInstalacion = new EntityDeletionOrUpdateAdapter<Instalacion>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `instalaciones` SET `id` = ?,`cantidad_personas` = ?,`category_id` = ?,`category_name` = ?,`description` = ?,`establecimiento_id` = ?,`name` = ?,`precio_hora` = ?,`portada` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Instalacion entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCantidad_personas() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getCantidad_personas());
        }
        if (entity.getCategory_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCategory_id());
        }
        if (entity.getCategory_name() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory_name());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        statement.bindLong(6, entity.getEstablecimiento_id());
        statement.bindString(7, entity.getName());
        if (entity.getPrecio_hora() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getPrecio_hora());
        }
        if (entity.getPortada() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPortada());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from instalaciones where id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM instalaciones";
        return _query;
      }
    };
    this.__upsertionAdapterOfInstalacion = new EntityUpsertionAdapter<Instalacion>(new EntityInsertionAdapter<Instalacion>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `instalaciones` (`id`,`cantidad_personas`,`category_id`,`category_name`,`description`,`establecimiento_id`,`name`,`precio_hora`,`portada`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Instalacion entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCantidad_personas() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getCantidad_personas());
        }
        if (entity.getCategory_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCategory_id());
        }
        if (entity.getCategory_name() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory_name());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        statement.bindLong(6, entity.getEstablecimiento_id());
        statement.bindString(7, entity.getName());
        if (entity.getPrecio_hora() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getPrecio_hora());
        }
        if (entity.getPortada() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPortada());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Instalacion>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `instalaciones` SET `id` = ?,`cantidad_personas` = ?,`category_id` = ?,`category_name` = ?,`description` = ?,`establecimiento_id` = ?,`name` = ?,`precio_hora` = ?,`portada` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Instalacion entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCantidad_personas() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getCantidad_personas());
        }
        if (entity.getCategory_id() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCategory_id());
        }
        if (entity.getCategory_name() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory_name());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        statement.bindLong(6, entity.getEstablecimiento_id());
        statement.bindString(7, entity.getName());
        if (entity.getPrecio_hora() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getPrecio_hora());
        }
        if (entity.getPortada() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPortada());
        }
        statement.bindLong(10, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final Instalacion entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfInstalacion.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Instalacion entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfInstalacion.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final long id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDelete.release(_stmt);
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
  public Object upsert(final Instalacion entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfInstalacion.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Instalacion[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfInstalacion.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Instalacion> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfInstalacion.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Instalacion>> observeInstalaciones(final long id) {
    final String _sql = "SELECT * FROM instalaciones WHERE establecimiento_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"instalaciones"}, new Callable<List<Instalacion>>() {
      @Override
      @NonNull
      public List<Instalacion> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
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
            final List<Instalacion> _result = new ArrayList<Instalacion>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Instalacion _item;
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
              _item = new Instalacion(_tmpId,_tmpCantidad_personas,_tmpCategory_id,_tmpCategory_name,_tmpDescription,_tmpEstablecimiento_id,_tmpName,_tmpPrecio_hora,_tmpPortada);
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
  public Flow<Instalacion> observeInstalacion(final long id) {
    final String _sql = "SELECT * FROM instalaciones WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"instalaciones"}, new Callable<Instalacion>() {
      @Override
      @NonNull
      public Instalacion call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
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
            final Instalacion _result;
            if (_cursor.moveToFirst()) {
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
              _result = new Instalacion(_tmpId,_tmpCantidad_personas,_tmpCategory_id,_tmpCategory_name,_tmpDescription,_tmpEstablecimiento_id,_tmpName,_tmpPrecio_hora,_tmpPortada);
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
  public Flow<List<Instalacion>> observeInstalacionesAvailables(final List<Long> ids) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM instalaciones WHERE id  in (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, true, new String[] {"instalaciones"}, new Callable<List<Instalacion>>() {
      @Override
      @NonNull
      public List<Instalacion> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
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
            final List<Instalacion> _result = new ArrayList<Instalacion>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Instalacion _item_1;
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
              _item_1 = new Instalacion(_tmpId,_tmpCantidad_personas,_tmpCategory_id,_tmpCategory_name,_tmpDescription,_tmpEstablecimiento_id,_tmpName,_tmpPrecio_hora,_tmpPortada);
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

  @Override
  public Flow<List<InstalacionCategoryCount>> observeGroupInstalacionByCategory(final long id,
      final LabelType type) {
    final String _sql = "select l.name,i.category_id,i.count,l.thumbnail from (select category_name,category_id,  count(category_id)  as count from instalaciones where establecimiento_id = ? group by category_id,category_name) as i inner join labels as l on l.id = i.category_id and type_label = ?;";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    _argIndex = 2;
    final String _tmp = AppTypeConverters.INSTANCE.fromLabelType(type);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"instalaciones",
        "labels"}, new Callable<List<InstalacionCategoryCount>>() {
      @Override
      @NonNull
      public List<InstalacionCategoryCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = 0;
          final int _cursorIndexOfCategoryId = 1;
          final int _cursorIndexOfCount = 2;
          final int _cursorIndexOfThumbnail = 3;
          final List<InstalacionCategoryCount> _result = new ArrayList<InstalacionCategoryCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InstalacionCategoryCount _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Integer _tmpCategory_id;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategory_id = null;
            } else {
              _tmpCategory_id = _cursor.getInt(_cursorIndexOfCategoryId);
            }
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            final String _tmpThumbnail;
            _tmpThumbnail = _cursor.getString(_cursorIndexOfThumbnail);
            _item = new InstalacionCategoryCount(_tmpName,_tmpCategory_id,_tmpCount,_tmpThumbnail);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
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
