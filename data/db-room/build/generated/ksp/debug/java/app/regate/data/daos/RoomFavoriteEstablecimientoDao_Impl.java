package app.regate.data.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.regate.models.FavoriteEstablecimiento;
import java.lang.Class;
import java.lang.Exception;
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

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomFavoriteEstablecimientoDao_Impl extends RoomFavoriteEstablecimientoDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento> __deletionAdapterOfFavoriteEstablecimiento;

  private final EntityDeletionOrUpdateAdapter<FavoriteEstablecimiento> __updateAdapterOfFavoriteEstablecimiento;

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
  public void observeFavoriteEstablecimiento() {
    final String _sql = "select * from favorite_establecimiento as fe inner join establecimientos as e on e.id = fe.establecimiento_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
      try {
        final void _result;
        if (_cursor.moveToFirst()) {
          _result = new Unit();
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
