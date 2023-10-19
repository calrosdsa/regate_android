package app.regate.data.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
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
import app.regate.compoundmodels.ReservaDetail;
import app.regate.data.db.AppTypeConverters;
import app.regate.data.db.DateTimeTypeConverters;
import app.regate.models.Establecimiento;
import app.regate.models.Instalacion;
import app.regate.models.Reserva;
import java.lang.Boolean;
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
public final class RoomReservaDao_Impl extends RoomReservaDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<Reserva> __deletionAdapterOfReserva;

  private final EntityDeletionOrUpdateAdapter<Reserva> __updateAdapterOfReserva;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<Reserva> __upsertionAdapterOfReserva;

  public RoomReservaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfReserva = new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `reservas` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Reserva entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReserva = new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `reservas` SET `id` = ?,`instalacion_id` = ?,`instalacion_name` = ?,`establecimiento_id` = ?,`pagado` = ?,`total_price` = ?,`start_date` = ?,`end_date` = ?,`user_id` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Reserva entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getInstalacion_id());
        statement.bindString(3, entity.getInstalacion_name());
        statement.bindLong(4, entity.getEstablecimiento_id());
        statement.bindDouble(5, entity.getPagado());
        statement.bindDouble(6, entity.getTotal_price());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getStart_date());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp);
        }
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getEnd_date());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_1);
        }
        statement.bindLong(9, entity.getUser_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from reservas";
        return _query;
      }
    };
    this.__upsertionAdapterOfReserva = new EntityUpsertionAdapter<Reserva>(new EntityInsertionAdapter<Reserva>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `reservas` (`id`,`instalacion_id`,`instalacion_name`,`establecimiento_id`,`pagado`,`total_price`,`start_date`,`end_date`,`user_id`,`created_at`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Reserva entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getInstalacion_id());
        statement.bindString(3, entity.getInstalacion_name());
        statement.bindLong(4, entity.getEstablecimiento_id());
        statement.bindDouble(5, entity.getPagado());
        statement.bindDouble(6, entity.getTotal_price());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getStart_date());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp);
        }
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getEnd_date());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_1);
        }
        statement.bindLong(9, entity.getUser_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
      }
    }, new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `reservas` SET `id` = ?,`instalacion_id` = ?,`instalacion_name` = ?,`establecimiento_id` = ?,`pagado` = ?,`total_price` = ?,`start_date` = ?,`end_date` = ?,`user_id` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Reserva entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getInstalacion_id());
        statement.bindString(3, entity.getInstalacion_name());
        statement.bindLong(4, entity.getEstablecimiento_id());
        statement.bindDouble(5, entity.getPagado());
        statement.bindDouble(6, entity.getTotal_price());
        final String _tmp = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getStart_date());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp);
        }
        final String _tmp_1 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getEnd_date());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_1);
        }
        statement.bindLong(9, entity.getUser_id());
        final String _tmp_2 = DateTimeTypeConverters.INSTANCE.fromInstant(entity.getCreated_at());
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
  public Object deleteEntity(final Reserva entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfReserva.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Reserva entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReserva.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Object upsert(final Reserva entity, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfReserva.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Reserva[] entity, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfReserva.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Reserva> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfReserva.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Reserva>> observeReservas() {
    final String _sql = "select * from reservas";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"reservas"}, new Callable<List<Reserva>>() {
      @Override
      @NonNull
      public List<Reserva> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfInstalacionId = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_id");
            final int _cursorIndexOfInstalacionName = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_name");
            final int _cursorIndexOfEstablecimientoId = CursorUtil.getColumnIndexOrThrow(_cursor, "establecimiento_id");
            final int _cursorIndexOfPagado = CursorUtil.getColumnIndexOrThrow(_cursor, "pagado");
            final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "total_price");
            final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
            final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final List<Reserva> _result = new ArrayList<Reserva>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Reserva _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpInstalacion_id;
              _tmpInstalacion_id = _cursor.getLong(_cursorIndexOfInstalacionId);
              final String _tmpInstalacion_name;
              _tmpInstalacion_name = _cursor.getString(_cursorIndexOfInstalacionName);
              final long _tmpEstablecimiento_id;
              _tmpEstablecimiento_id = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              final double _tmpPagado;
              _tmpPagado = _cursor.getDouble(_cursorIndexOfPagado);
              final double _tmpTotal_price;
              _tmpTotal_price = _cursor.getDouble(_cursorIndexOfTotalPrice);
              final Instant _tmpStart_date;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfStartDate)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfStartDate);
              }
              final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpStart_date = _tmp_1;
              }
              final Instant _tmpEnd_date;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfEndDate)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfEndDate);
              }
              final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpEnd_date = _tmp_3;
              }
              final long _tmpUser_id;
              _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
              final Instant _tmpCreated_at;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              final Instant _tmp_5 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpCreated_at = _tmp_5;
              }
              _item = new Reserva(_tmpId,_tmpInstalacion_id,_tmpInstalacion_name,_tmpEstablecimiento_id,_tmpPagado,_tmpTotal_price,_tmpStart_date,_tmpEnd_date,_tmpUser_id,_tmpCreated_at);
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
  public Flow<ReservaDetail> observeReservaDetail(final long id) {
    final String _sql = "select * from reservas where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"establecimientos", "instalaciones",
        "reservas"}, new Callable<ReservaDetail>() {
      @Override
      @NonNull
      public ReservaDetail call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfInstalacionId = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_id");
            final int _cursorIndexOfInstalacionName = CursorUtil.getColumnIndexOrThrow(_cursor, "instalacion_name");
            final int _cursorIndexOfEstablecimientoId = CursorUtil.getColumnIndexOrThrow(_cursor, "establecimiento_id");
            final int _cursorIndexOfPagado = CursorUtil.getColumnIndexOrThrow(_cursor, "pagado");
            final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "total_price");
            final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
            final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final LongSparseArray<Establecimiento> _collectionEstablecimiento = new LongSparseArray<Establecimiento>();
            final LongSparseArray<Instalacion> _collectionInstalacion = new LongSparseArray<Instalacion>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              _collectionEstablecimiento.put(_tmpKey, null);
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfInstalacionId);
              _collectionInstalacion.put(_tmpKey_1, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipestablecimientosAsappRegateModelsEstablecimiento(_collectionEstablecimiento);
            __fetchRelationshipinstalacionesAsappRegateModelsInstalacion(_collectionInstalacion);
            final ReservaDetail _result;
            if (_cursor.moveToFirst()) {
              final Reserva _tmpReserva;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpInstalacion_id;
              _tmpInstalacion_id = _cursor.getLong(_cursorIndexOfInstalacionId);
              final String _tmpInstalacion_name;
              _tmpInstalacion_name = _cursor.getString(_cursorIndexOfInstalacionName);
              final long _tmpEstablecimiento_id;
              _tmpEstablecimiento_id = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              final double _tmpPagado;
              _tmpPagado = _cursor.getDouble(_cursorIndexOfPagado);
              final double _tmpTotal_price;
              _tmpTotal_price = _cursor.getDouble(_cursorIndexOfTotalPrice);
              final Instant _tmpStart_date;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfStartDate)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfStartDate);
              }
              final Instant _tmp_1 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpStart_date = _tmp_1;
              }
              final Instant _tmpEnd_date;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfEndDate)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfEndDate);
              }
              final Instant _tmp_3 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpEnd_date = _tmp_3;
              }
              final long _tmpUser_id;
              _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
              final Instant _tmpCreated_at;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfCreatedAt);
              }
              final Instant _tmp_5 = DateTimeTypeConverters.INSTANCE.toInstant(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected non-null kotlinx.datetime.Instant, but it was null.");
              } else {
                _tmpCreated_at = _tmp_5;
              }
              _tmpReserva = new Reserva(_tmpId,_tmpInstalacion_id,_tmpInstalacion_name,_tmpEstablecimiento_id,_tmpPagado,_tmpTotal_price,_tmpStart_date,_tmpEnd_date,_tmpUser_id,_tmpCreated_at);
              final Establecimiento _tmpEstablecimiento;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              _tmpEstablecimiento = _collectionEstablecimiento.get(_tmpKey_2);
              final Instalacion _tmpInstalacion;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfInstalacionId);
              _tmpInstalacion = _collectionInstalacion.get(_tmpKey_3);
              _result = new ReservaDetail();
              _result.reserva = _tmpReserva;
              _result.setEstablecimiento(_tmpEstablecimiento);
              _result.setInstalacion(_tmpInstalacion);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipestablecimientosAsappRegateModelsEstablecimiento(
      @NonNull final LongSparseArray<Establecimiento> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipestablecimientosAsappRegateModelsEstablecimiento(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`address`,`created_at`,`email`,`empresa_id`,`latidud`,`longitud`,`name`,`description`,`is_open`,`phone_number`,`photo`,`address_photo`,`amenities`,`rules` FROM `establecimientos` WHERE `id` IN (");
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
      final int _cursorIndexOfAddress = 1;
      final int _cursorIndexOfCreatedAt = 2;
      final int _cursorIndexOfEmail = 3;
      final int _cursorIndexOfEmpresaId = 4;
      final int _cursorIndexOfLatidud = 5;
      final int _cursorIndexOfLongitud = 6;
      final int _cursorIndexOfName = 7;
      final int _cursorIndexOfDescription = 8;
      final int _cursorIndexOfIsOpen = 9;
      final int _cursorIndexOfPhoneNumber = 10;
      final int _cursorIndexOfPhoto = 11;
      final int _cursorIndexOfAddressPhoto = 12;
      final int _cursorIndexOfAmenities = 13;
      final int _cursorIndexOfRules = 14;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Establecimiento _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpAddress;
          if (_cursor.isNull(_cursorIndexOfAddress)) {
            _tmpAddress = null;
          } else {
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
          }
          final String _tmpCreated_at;
          if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmpCreated_at = null;
          } else {
            _tmpCreated_at = _cursor.getString(_cursorIndexOfCreatedAt);
          }
          final String _tmpEmail;
          if (_cursor.isNull(_cursorIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
          }
          final Integer _tmpEmpresa_id;
          if (_cursor.isNull(_cursorIndexOfEmpresaId)) {
            _tmpEmpresa_id = null;
          } else {
            _tmpEmpresa_id = _cursor.getInt(_cursorIndexOfEmpresaId);
          }
          final String _tmpLatidud;
          if (_cursor.isNull(_cursorIndexOfLatidud)) {
            _tmpLatidud = null;
          } else {
            _tmpLatidud = _cursor.getString(_cursorIndexOfLatidud);
          }
          final String _tmpLongitud;
          if (_cursor.isNull(_cursorIndexOfLongitud)) {
            _tmpLongitud = null;
          } else {
            _tmpLongitud = _cursor.getString(_cursorIndexOfLongitud);
          }
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpDescription;
          if (_cursor.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
          }
          final Boolean _tmpIs_open;
          final Integer _tmp;
          if (_cursor.isNull(_cursorIndexOfIsOpen)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getInt(_cursorIndexOfIsOpen);
          }
          _tmpIs_open = _tmp == null ? null : _tmp != 0;
          final String _tmpPhone_number;
          if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
            _tmpPhone_number = null;
          } else {
            _tmpPhone_number = _cursor.getString(_cursorIndexOfPhoneNumber);
          }
          final String _tmpPhoto;
          if (_cursor.isNull(_cursorIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _cursor.getString(_cursorIndexOfPhoto);
          }
          final String _tmpAddress_photo;
          if (_cursor.isNull(_cursorIndexOfAddressPhoto)) {
            _tmpAddress_photo = null;
          } else {
            _tmpAddress_photo = _cursor.getString(_cursorIndexOfAddressPhoto);
          }
          final List<Long> _tmpAmenities;
          final String _tmp_1;
          _tmp_1 = _cursor.getString(_cursorIndexOfAmenities);
          _tmpAmenities = AppTypeConverters.INSTANCE.toListLong(_tmp_1);
          final List<Long> _tmpRules;
          final String _tmp_2;
          _tmp_2 = _cursor.getString(_cursorIndexOfRules);
          _tmpRules = AppTypeConverters.INSTANCE.toListLong(_tmp_2);
          _item_1 = new Establecimiento(_tmpId,_tmpAddress,_tmpCreated_at,_tmpEmail,_tmpEmpresa_id,_tmpLatidud,_tmpLongitud,_tmpName,_tmpDescription,_tmpIs_open,_tmpPhone_number,_tmpPhoto,_tmpAddress_photo,_tmpAmenities,_tmpRules);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipinstalacionesAsappRegateModelsInstalacion(
      @NonNull final LongSparseArray<Instalacion> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipinstalacionesAsappRegateModelsInstalacion(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`cantidad_personas`,`category_id`,`category_name`,`description`,`establecimiento_id`,`name`,`precio_hora`,`portada` FROM `instalaciones` WHERE `id` IN (");
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
      final int _cursorIndexOfCantidadPersonas = 1;
      final int _cursorIndexOfCategoryId = 2;
      final int _cursorIndexOfCategoryName = 3;
      final int _cursorIndexOfDescription = 4;
      final int _cursorIndexOfEstablecimientoId = 5;
      final int _cursorIndexOfName = 6;
      final int _cursorIndexOfPrecioHora = 7;
      final int _cursorIndexOfPortada = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
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
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
