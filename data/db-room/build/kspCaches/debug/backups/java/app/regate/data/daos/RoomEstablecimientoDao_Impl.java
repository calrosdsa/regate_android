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
import app.regate.data.dto.empresa.establecimiento.HorarioInterval;
import app.regate.data.dto.empresa.establecimiento.PaidType;
import app.regate.models.Establecimiento;
import app.regate.models.Setting;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
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
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomEstablecimientoDao_Impl extends RoomEstablecimientoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Setting> __insertionAdapterOfSetting;

  private final EntityDeletionOrUpdateAdapter<Establecimiento> __deletionAdapterOfEstablecimiento;

  private final EntityDeletionOrUpdateAdapter<Establecimiento> __updateAdapterOfEstablecimiento;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<Establecimiento> __upsertionAdapterOfEstablecimiento;

  public RoomEstablecimientoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSetting = new EntityInsertionAdapter<Setting>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT OR REPLACE INTO `settings` (`uuid`,`paid_type`,`establecimiento_id`,`payment_for_reservation`,`horario_interval`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Setting entity) {
        statement.bindString(1, entity.getUuid());
        final String _tmp;
        if (entity.getPaid_type() == null) {
          _tmp = null;
        } else {
          _tmp = AppTypeConverters.INSTANCE.fromPaidType(entity.getPaid_type());
        }
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindLong(3, entity.getEstablecimiento_id());
        if (entity.getPayment_for_reservation() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getPayment_for_reservation());
        }
        final String _tmp_1 = AppTypeConverters.INSTANCE.fromListHorario(entity.getHorario_interval());
        statement.bindString(5, _tmp_1);
      }
    };
    this.__deletionAdapterOfEstablecimiento = new EntityDeletionOrUpdateAdapter<Establecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `establecimientos` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Establecimiento entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfEstablecimiento = new EntityDeletionOrUpdateAdapter<Establecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `establecimientos` SET `id` = ?,`address` = ?,`created_at` = ?,`email` = ?,`empresa_id` = ?,`latidud` = ?,`longitud` = ?,`name` = ?,`description` = ?,`is_open` = ?,`phone_number` = ?,`photo` = ?,`address_photo` = ?,`amenities` = ?,`rules` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Establecimiento entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAddress());
        }
        if (entity.getCreated_at() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCreated_at());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEmpresa_id() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEmpresa_id());
        }
        if (entity.getLatidud() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLatidud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLongitud());
        }
        statement.bindString(8, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        final Integer _tmp = entity.is_open() == null ? null : (entity.is_open() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp);
        }
        if (entity.getPhone_number() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPhone_number());
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhoto());
        }
        if (entity.getAddress_photo() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAddress_photo());
        }
        final String _tmp_1 = AppTypeConverters.INSTANCE.fromListLong(entity.getAmenities());
        statement.bindString(14, _tmp_1);
        final String _tmp_2 = AppTypeConverters.INSTANCE.fromListLong(entity.getRules());
        statement.bindString(15, _tmp_2);
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM establecimientos where id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM establecimientos";
        return _query;
      }
    };
    this.__upsertionAdapterOfEstablecimiento = new EntityUpsertionAdapter<Establecimiento>(new EntityInsertionAdapter<Establecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `establecimientos` (`id`,`address`,`created_at`,`email`,`empresa_id`,`latidud`,`longitud`,`name`,`description`,`is_open`,`phone_number`,`photo`,`address_photo`,`amenities`,`rules`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Establecimiento entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAddress());
        }
        if (entity.getCreated_at() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCreated_at());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEmpresa_id() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEmpresa_id());
        }
        if (entity.getLatidud() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLatidud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLongitud());
        }
        statement.bindString(8, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        final Integer _tmp = entity.is_open() == null ? null : (entity.is_open() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp);
        }
        if (entity.getPhone_number() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPhone_number());
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhoto());
        }
        if (entity.getAddress_photo() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAddress_photo());
        }
        final String _tmp_1 = AppTypeConverters.INSTANCE.fromListLong(entity.getAmenities());
        statement.bindString(14, _tmp_1);
        final String _tmp_2 = AppTypeConverters.INSTANCE.fromListLong(entity.getRules());
        statement.bindString(15, _tmp_2);
      }
    }, new EntityDeletionOrUpdateAdapter<Establecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `establecimientos` SET `id` = ?,`address` = ?,`created_at` = ?,`email` = ?,`empresa_id` = ?,`latidud` = ?,`longitud` = ?,`name` = ?,`description` = ?,`is_open` = ?,`phone_number` = ?,`photo` = ?,`address_photo` = ?,`amenities` = ?,`rules` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Establecimiento entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAddress());
        }
        if (entity.getCreated_at() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCreated_at());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEmail());
        }
        if (entity.getEmpresa_id() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEmpresa_id());
        }
        if (entity.getLatidud() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLatidud());
        }
        if (entity.getLongitud() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLongitud());
        }
        statement.bindString(8, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        final Integer _tmp = entity.is_open() == null ? null : (entity.is_open() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp);
        }
        if (entity.getPhone_number() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPhone_number());
        }
        if (entity.getPhoto() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhoto());
        }
        if (entity.getAddress_photo() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAddress_photo());
        }
        final String _tmp_1 = AppTypeConverters.INSTANCE.fromListLong(entity.getAmenities());
        statement.bindString(14, _tmp_1);
        final String _tmp_2 = AppTypeConverters.INSTANCE.fromListLong(entity.getRules());
        statement.bindString(15, _tmp_2);
        statement.bindLong(16, entity.getId());
      }
    });
  }

  @Override
  public Object insertSettingEstablecimiento(final Setting entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSetting.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEntity(final Establecimiento entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfEstablecimiento.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final Establecimiento entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEstablecimiento.handle(entity);
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
  public Object upsert(final Establecimiento entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfEstablecimiento.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final Establecimiento[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfEstablecimiento.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends Establecimiento> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfEstablecimiento.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<Establecimiento> getEstablecimiento(final long id) {
    final String _sql = "SELECT * FROM establecimientos where id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"establecimientos"}, new Callable<Establecimiento>() {
      @Override
      @NonNull
      public Establecimiento call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfEmpresaId = CursorUtil.getColumnIndexOrThrow(_cursor, "empresa_id");
            final int _cursorIndexOfLatidud = CursorUtil.getColumnIndexOrThrow(_cursor, "latidud");
            final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfIsOpen = CursorUtil.getColumnIndexOrThrow(_cursor, "is_open");
            final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfAddressPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "address_photo");
            final int _cursorIndexOfAmenities = CursorUtil.getColumnIndexOrThrow(_cursor, "amenities");
            final int _cursorIndexOfRules = CursorUtil.getColumnIndexOrThrow(_cursor, "rules");
            final Establecimiento _result;
            if (_cursor.moveToFirst()) {
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
              _result = new Establecimiento(_tmpId,_tmpAddress,_tmpCreated_at,_tmpEmail,_tmpEmpresa_id,_tmpLatidud,_tmpLongitud,_tmpName,_tmpDescription,_tmpIs_open,_tmpPhone_number,_tmpPhoto,_tmpAddress_photo,_tmpAmenities,_tmpRules);
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
  public Flow<List<Establecimiento>> getEstablecimientos() {
    final String _sql = "SELECT * FROM establecimientos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"establecimientos"}, new Callable<List<Establecimiento>>() {
      @Override
      @NonNull
      public List<Establecimiento> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfEmpresaId = CursorUtil.getColumnIndexOrThrow(_cursor, "empresa_id");
            final int _cursorIndexOfLatidud = CursorUtil.getColumnIndexOrThrow(_cursor, "latidud");
            final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfIsOpen = CursorUtil.getColumnIndexOrThrow(_cursor, "is_open");
            final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfAddressPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "address_photo");
            final int _cursorIndexOfAmenities = CursorUtil.getColumnIndexOrThrow(_cursor, "amenities");
            final int _cursorIndexOfRules = CursorUtil.getColumnIndexOrThrow(_cursor, "rules");
            final List<Establecimiento> _result = new ArrayList<Establecimiento>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Establecimiento _item;
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
              _item = new Establecimiento(_tmpId,_tmpAddress,_tmpCreated_at,_tmpEmail,_tmpEmpresa_id,_tmpLatidud,_tmpLongitud,_tmpName,_tmpDescription,_tmpIs_open,_tmpPhone_number,_tmpPhoto,_tmpAddress_photo,_tmpAmenities,_tmpRules);
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
  public Flow<Establecimiento> observeEstablecimiento(final long id) {
    final String _sql = "SELECT * FROM establecimientos WHERE  id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"establecimientos"}, new Callable<Establecimiento>() {
      @Override
      @NonNull
      public Establecimiento call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
            final int _cursorIndexOfEmpresaId = CursorUtil.getColumnIndexOrThrow(_cursor, "empresa_id");
            final int _cursorIndexOfLatidud = CursorUtil.getColumnIndexOrThrow(_cursor, "latidud");
            final int _cursorIndexOfLongitud = CursorUtil.getColumnIndexOrThrow(_cursor, "longitud");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfIsOpen = CursorUtil.getColumnIndexOrThrow(_cursor, "is_open");
            final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
            final int _cursorIndexOfPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
            final int _cursorIndexOfAddressPhoto = CursorUtil.getColumnIndexOrThrow(_cursor, "address_photo");
            final int _cursorIndexOfAmenities = CursorUtil.getColumnIndexOrThrow(_cursor, "amenities");
            final int _cursorIndexOfRules = CursorUtil.getColumnIndexOrThrow(_cursor, "rules");
            final Establecimiento _result;
            if (_cursor.moveToFirst()) {
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
              _result = new Establecimiento(_tmpId,_tmpAddress,_tmpCreated_at,_tmpEmail,_tmpEmpresa_id,_tmpLatidud,_tmpLongitud,_tmpName,_tmpDescription,_tmpIs_open,_tmpPhone_number,_tmpPhoto,_tmpAddress_photo,_tmpAmenities,_tmpRules);
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
  public Flow<Setting> observeEstablecimientoSetting(final long id) {
    final String _sql = "SELECT * FROM settings WHERE establecimiento_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"settings"}, new Callable<Setting>() {
      @Override
      @NonNull
      public Setting call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
            final int _cursorIndexOfPaidType = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_type");
            final int _cursorIndexOfEstablecimientoId = CursorUtil.getColumnIndexOrThrow(_cursor, "establecimiento_id");
            final int _cursorIndexOfPaymentForReservation = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_for_reservation");
            final int _cursorIndexOfHorarioInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "horario_interval");
            final Setting _result;
            if (_cursor.moveToFirst()) {
              final String _tmpUuid;
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
              final PaidType _tmpPaid_type;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfPaidType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfPaidType);
              }
              if (_tmp == null) {
                _tmpPaid_type = null;
              } else {
                _tmpPaid_type = AppTypeConverters.INSTANCE.toPaidType(_tmp);
              }
              final long _tmpEstablecimiento_id;
              _tmpEstablecimiento_id = _cursor.getLong(_cursorIndexOfEstablecimientoId);
              final Integer _tmpPayment_for_reservation;
              if (_cursor.isNull(_cursorIndexOfPaymentForReservation)) {
                _tmpPayment_for_reservation = null;
              } else {
                _tmpPayment_for_reservation = _cursor.getInt(_cursorIndexOfPaymentForReservation);
              }
              final List<HorarioInterval> _tmpHorario_interval;
              final String _tmp_1;
              _tmp_1 = _cursor.getString(_cursorIndexOfHorarioInterval);
              _tmpHorario_interval = AppTypeConverters.INSTANCE.toListHorario(_tmp_1);
              _result = new Setting(_tmpUuid,_tmpPaid_type,_tmpEstablecimiento_id,_tmpPayment_for_reservation,_tmpHorario_interval);
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
}
