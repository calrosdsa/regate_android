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
import app.regate.models.Establecimiento;
import app.regate.models.FavoriteEstablecimiento;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomFavoriteEstablecimientoDao_Impl extends RoomFavoriteEstablecimientoDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento> __deletionAdapterOfFavoriteEstablecimiento;

  private final EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento> __updateAdapterOfFavoriteEstablecimiento;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveFavoriteEstablecimiento;

  private final EntityUpsertionAdapter<FavoriteEstablecimiento> __upsertionAdapterOfFavoriteEstablecimiento;

  public RoomFavoriteEstablecimientoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfFavoriteEstablecimiento = new EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "DELETE FROM `favorite_establecimiento` WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteEstablecimiento entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFavoriteEstablecimiento = new EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE OR ABORT `favorite_establecimiento` SET `id` = ?,`establecimiento_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteEstablecimiento entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEstablecimiento_id());
        statement.bindLong(3, entity.getId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from favorite_establecimiento";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveFavoriteEstablecimiento = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "delete from favorite_establecimiento where establecimiento_id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfFavoriteEstablecimiento = new EntityUpsertionAdapter<FavoriteEstablecimiento>(new EntityInsertionAdapter<FavoriteEstablecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "INSERT INTO `favorite_establecimiento` (`id`,`establecimiento_id`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteEstablecimiento entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEstablecimiento_id());
      }
    }, new EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento>(__db) {
      @Override
      @NonNull
      public String createQuery() {
        return "UPDATE `favorite_establecimiento` SET `id` = ?,`establecimiento_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteEstablecimiento entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEstablecimiento_id());
        statement.bindLong(3, entity.getId());
      }
    });
  }

  @Override
  public Object deleteEntity(final FavoriteEstablecimiento entity,
      final Continuation<? super Integer> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfFavoriteEstablecimiento.handle(entity);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final FavoriteEstablecimiento entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFavoriteEstablecimiento.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public void removeAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAll.release(_stmt);
    }
  }

  @Override
  public void removeFavoriteEstablecimiento(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveFavoriteEstablecimiento.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveFavoriteEstablecimiento.release(_stmt);
    }
  }

  @Override
  public Object upsert(final FavoriteEstablecimiento entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __upsertionAdapterOfFavoriteEstablecimiento.upsertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final FavoriteEstablecimiento[] entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfFavoriteEstablecimiento.upsert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertAll(final List<? extends FavoriteEstablecimiento> entities,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfFavoriteEstablecimiento.upsert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Establecimiento>> observeFavoriteEstablecimiento() {
    final String _sql = "select e.* from favorite_establecimiento as fe inner join establecimientos as e on e.id = fe.establecimiento_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"favorite_establecimiento",
        "establecimientos"}, new Callable<List<Establecimiento>>() {
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
              final List<Long> _tmpAmenities;
              final String _tmp_1;
              _tmp_1 = _cursor.getString(_cursorIndexOfAmenities);
              _tmpAmenities = AppTypeConverters.INSTANCE.toListLong(_tmp_1);
              final List<Long> _tmpRules;
              final String _tmp_2;
              _tmp_2 = _cursor.getString(_cursorIndexOfRules);
              _tmpRules = AppTypeConverters.INSTANCE.toListLong(_tmp_2);
              _item = new Establecimiento(_tmpId,_tmpAddress,_tmpCreated_at,_tmpEmail,_tmpEmpresa_id,_tmpLatidud,_tmpLongitud,_tmpName,_tmpDescription,_tmpIs_open,_tmpPhone_number,_tmpPhoto,_tmpAmenities,_tmpRules);
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
  public Flow<List<Long>> observeFavoriteEstablecimientosIds() {
    final String _sql = "select establecimiento_id from favorite_establecimiento";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"favorite_establecimiento"}, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Long _item;
              _item = _cursor.getLong(0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
