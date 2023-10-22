package app.regate.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import app.regate.data.daos.RoomCupoDao;
import app.regate.data.daos.RoomCupoDao_Impl;
import app.regate.data.daos.RoomEmojiDao;
import app.regate.data.daos.RoomEmojiDao_Impl;
import app.regate.data.daos.RoomEstablecimientoDao;
import app.regate.data.daos.RoomEstablecimientoDao_Impl;
import app.regate.data.daos.RoomFavoriteEstablecimientoDao;
import app.regate.data.daos.RoomFavoriteEstablecimientoDao_Impl;
import app.regate.data.daos.RoomGrupoDao;
import app.regate.data.daos.RoomGrupoDao_Impl;
import app.regate.data.daos.RoomInstalacionDao;
import app.regate.data.daos.RoomInstalacionDao_Impl;
import app.regate.data.daos.RoomLabelDao;
import app.regate.data.daos.RoomLabelDao_Impl;
import app.regate.data.daos.RoomMessageInboxDao;
import app.regate.data.daos.RoomMessageInboxDao_Impl;
import app.regate.data.daos.RoomMessageProfileDao;
import app.regate.data.daos.RoomMessageProfileDao_Impl;
import app.regate.data.daos.RoomMessageSalaDao;
import app.regate.data.daos.RoomMessageSalaDao_Impl;
import app.regate.data.daos.RoomMyGroupsDao;
import app.regate.data.daos.RoomMyGroupsDao_Impl;
import app.regate.data.daos.RoomNotificationDao;
import app.regate.data.daos.RoomNotificationDao_Impl;
import app.regate.data.daos.RoomProfileDao;
import app.regate.data.daos.RoomProfileDao_Impl;
import app.regate.data.daos.RoomReservaDao;
import app.regate.data.daos.RoomReservaDao_Impl;
import app.regate.data.daos.RoomSearchHistoryDao;
import app.regate.data.daos.RoomSearchHistoryDao_Impl;
import app.regate.data.daos.RoomUserDao;
import app.regate.data.daos.RoomUserDao_Impl;
import app.regate.data.daos.RoomUserGrupoDao;
import app.regate.data.daos.RoomUserGrupoDao_Impl;
import app.regate.data.daos.RoomUserRoomDao;
import app.regate.data.daos.RoomUserRoomDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppRoomDatabase_Impl extends AppRoomDatabase {
  private volatile RoomEstablecimientoDao _roomEstablecimientoDao;

  private volatile RoomInstalacionDao _roomInstalacionDao;

  private volatile RoomCupoDao _roomCupoDao;

  private volatile RoomUserDao _roomUserDao;

  private volatile RoomProfileDao _roomProfileDao;

  private volatile RoomMessageProfileDao _roomMessageProfileDao;

  private volatile RoomLabelDao _roomLabelDao;

  private volatile RoomGrupoDao _roomGrupoDao;

  private volatile RoomUserGrupoDao _roomUserGrupoDao;

  private volatile RoomUserRoomDao _roomUserRoomDao;

  private volatile RoomMyGroupsDao _roomMyGroupsDao;

  private volatile RoomFavoriteEstablecimientoDao _roomFavoriteEstablecimientoDao;

  private volatile RoomMessageInboxDao _roomMessageInboxDao;

  private volatile RoomReservaDao _roomReservaDao;

  private volatile RoomNotificationDao _roomNotificationDao;

  private volatile RoomMessageSalaDao _roomMessageSalaDao;

  private volatile RoomSearchHistoryDao _roomSearchHistoryDao;

  private volatile RoomEmojiDao _roomEmojiDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `establecimientos` (`id` INTEGER NOT NULL, `address` TEXT, `created_at` TEXT, `email` TEXT, `empresa_id` INTEGER, `latidud` TEXT, `longitud` TEXT, `name` TEXT NOT NULL, `description` TEXT, `is_open` INTEGER, `phone_number` TEXT, `photo` TEXT, `address_photo` TEXT, `amenities` TEXT NOT NULL, `rules` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `instalaciones` (`id` INTEGER NOT NULL, `cantidad_personas` INTEGER, `category_id` INTEGER, `category_name` TEXT, `description` TEXT, `establecimiento_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `precio_hora` INTEGER, `portada` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`establecimiento_id`) REFERENCES `establecimientos`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_instalaciones_establecimiento_id` ON `instalaciones` (`establecimiento_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cupos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `time` TEXT NOT NULL, `instalacion_id` INTEGER NOT NULL, `price` REAL NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cupos_instalacion_id` ON `cupos` (`instalacion_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `email` TEXT NOT NULL, `estado` INTEGER NOT NULL, `username` TEXT NOT NULL, `profile_id` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` INTEGER NOT NULL, `grupo_id` INTEGER NOT NULL, `content` TEXT NOT NULL, `data` TEXT, `created_at` TEXT NOT NULL, `type_message` INTEGER NOT NULL, `profile_id` INTEGER NOT NULL, `reply_to` INTEGER, `sended` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`id` INTEGER NOT NULL, `uuid` TEXT NOT NULL, `user_id` INTEGER, `email` TEXT, `profile_photo` TEXT, `nombre` TEXT NOT NULL, `apellido` TEXT, `created_at` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`uuid` TEXT NOT NULL, `paid_type` TEXT, `establecimiento_id` INTEGER NOT NULL, `payment_for_reservation` INTEGER, `horario_interval` TEXT NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`establecimiento_id`) REFERENCES `establecimientos`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_settings_establecimiento_id` ON `settings` (`establecimiento_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `labels` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `thumbnail` TEXT, `type_label` TEXT, PRIMARY KEY(`id`, `name`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `grupos` (`id` INTEGER NOT NULL, `uuid` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `created_at` TEXT, `photo` TEXT, `is_visible` INTEGER NOT NULL, `profile_id` INTEGER NOT NULL, `visibility` INTEGER NOT NULL, `last_message` TEXT NOT NULL, `last_message_created` TEXT, `messages_count` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_grupo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profile_id` INTEGER NOT NULL, `grupo_id` INTEGER NOT NULL, `is_admin` INTEGER NOT NULL, FOREIGN KEY(`grupo_id`) REFERENCES `grupos`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_user_grupo_grupo_id` ON `user_grupo` (`grupo_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `my_groups` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `group_id` INTEGER NOT NULL, `request_estado` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_establecimiento` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `establecimiento_id` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `message_inbox` (`id` INTEGER NOT NULL, `conversation_id` INTEGER NOT NULL, `content` TEXT NOT NULL, `created_at` TEXT NOT NULL, `sender_id` INTEGER NOT NULL, `reply_to` INTEGER, `sended` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reservas` (`id` INTEGER NOT NULL, `instalacion_id` INTEGER NOT NULL, `instalacion_name` TEXT NOT NULL, `establecimiento_id` INTEGER NOT NULL, `pagado` REAL NOT NULL, `total_price` REAL NOT NULL, `start_date` TEXT NOT NULL, `end_date` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `created_at` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notification` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `content` TEXT NOT NULL, `entityId` INTEGER, `typeEntity` INTEGER, `read` INTEGER NOT NULL, `created_at` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `message_sala` (`id` INTEGER NOT NULL, `sala_id` INTEGER NOT NULL, `content` TEXT NOT NULL, `created_at` TEXT NOT NULL, `profile_id` INTEGER NOT NULL, `reply_to` INTEGER, `sended` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `search_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `query` TEXT NOT NULL, `created_at` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `emoji` (`id` INTEGER NOT NULL, `emoji` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attention_schedule` (`id` INTEGER NOT NULL, `day_week` INTEGER NOT NULL, `establecimiento_id` INTEGER NOT NULL, `open` INTEGER NOT NULL, `closed` INTEGER NOT NULL, `schedule_interval` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`establecimiento_id`) REFERENCES `establecimientos`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attention_schedule_establecimiento_id` ON `attention_schedule` (`establecimiento_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_room` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profile_id` INTEGER NOT NULL, `entity_id` INTEGER NOT NULL, `is_admin` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_balance` (`balance_id` INTEGER NOT NULL, `profile_id` INTEGER NOT NULL, `coins` REAL NOT NULL, PRIMARY KEY(`balance_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ea0e1278321de7b33d516f9b237cbe0d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `establecimientos`");
        db.execSQL("DROP TABLE IF EXISTS `instalaciones`");
        db.execSQL("DROP TABLE IF EXISTS `cupos`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `profiles`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        db.execSQL("DROP TABLE IF EXISTS `labels`");
        db.execSQL("DROP TABLE IF EXISTS `grupos`");
        db.execSQL("DROP TABLE IF EXISTS `user_grupo`");
        db.execSQL("DROP TABLE IF EXISTS `my_groups`");
        db.execSQL("DROP TABLE IF EXISTS `favorite_establecimiento`");
        db.execSQL("DROP TABLE IF EXISTS `message_inbox`");
        db.execSQL("DROP TABLE IF EXISTS `reservas`");
        db.execSQL("DROP TABLE IF EXISTS `notification`");
        db.execSQL("DROP TABLE IF EXISTS `message_sala`");
        db.execSQL("DROP TABLE IF EXISTS `search_history`");
        db.execSQL("DROP TABLE IF EXISTS `emoji`");
        db.execSQL("DROP TABLE IF EXISTS `attention_schedule`");
        db.execSQL("DROP TABLE IF EXISTS `user_room`");
        db.execSQL("DROP TABLE IF EXISTS `user_balance`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsEstablecimientos = new HashMap<String, TableInfo.Column>(15);
        _columnsEstablecimientos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("empresa_id", new TableInfo.Column("empresa_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("latidud", new TableInfo.Column("latidud", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("longitud", new TableInfo.Column("longitud", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("is_open", new TableInfo.Column("is_open", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("phone_number", new TableInfo.Column("phone_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("photo", new TableInfo.Column("photo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("address_photo", new TableInfo.Column("address_photo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("amenities", new TableInfo.Column("amenities", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstablecimientos.put("rules", new TableInfo.Column("rules", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEstablecimientos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEstablecimientos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEstablecimientos = new TableInfo("establecimientos", _columnsEstablecimientos, _foreignKeysEstablecimientos, _indicesEstablecimientos);
        final TableInfo _existingEstablecimientos = TableInfo.read(db, "establecimientos");
        if (!_infoEstablecimientos.equals(_existingEstablecimientos)) {
          return new RoomOpenHelper.ValidationResult(false, "establecimientos(app.regate.models.Establecimiento).\n"
                  + " Expected:\n" + _infoEstablecimientos + "\n"
                  + " Found:\n" + _existingEstablecimientos);
        }
        final HashMap<String, TableInfo.Column> _columnsInstalaciones = new HashMap<String, TableInfo.Column>(9);
        _columnsInstalaciones.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("cantidad_personas", new TableInfo.Column("cantidad_personas", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("category_id", new TableInfo.Column("category_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("category_name", new TableInfo.Column("category_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("establecimiento_id", new TableInfo.Column("establecimiento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("precio_hora", new TableInfo.Column("precio_hora", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstalaciones.put("portada", new TableInfo.Column("portada", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInstalaciones = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysInstalaciones.add(new TableInfo.ForeignKey("establecimientos", "CASCADE", "CASCADE", Arrays.asList("establecimiento_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesInstalaciones = new HashSet<TableInfo.Index>(1);
        _indicesInstalaciones.add(new TableInfo.Index("index_instalaciones_establecimiento_id", false, Arrays.asList("establecimiento_id"), Arrays.asList("ASC")));
        final TableInfo _infoInstalaciones = new TableInfo("instalaciones", _columnsInstalaciones, _foreignKeysInstalaciones, _indicesInstalaciones);
        final TableInfo _existingInstalaciones = TableInfo.read(db, "instalaciones");
        if (!_infoInstalaciones.equals(_existingInstalaciones)) {
          return new RoomOpenHelper.ValidationResult(false, "instalaciones(app.regate.models.Instalacion).\n"
                  + " Expected:\n" + _infoInstalaciones + "\n"
                  + " Found:\n" + _existingInstalaciones);
        }
        final HashMap<String, TableInfo.Column> _columnsCupos = new HashMap<String, TableInfo.Column>(4);
        _columnsCupos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCupos.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCupos.put("instalacion_id", new TableInfo.Column("instalacion_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCupos.put("price", new TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCupos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCupos = new HashSet<TableInfo.Index>(1);
        _indicesCupos.add(new TableInfo.Index("index_cupos_instalacion_id", false, Arrays.asList("instalacion_id"), Arrays.asList("ASC")));
        final TableInfo _infoCupos = new TableInfo("cupos", _columnsCupos, _foreignKeysCupos, _indicesCupos);
        final TableInfo _existingCupos = TableInfo.read(db, "cupos");
        if (!_infoCupos.equals(_existingCupos)) {
          return new RoomOpenHelper.ValidationResult(false, "cupos(app.regate.models.Cupo).\n"
                  + " Expected:\n" + _infoCupos + "\n"
                  + " Found:\n" + _existingCupos);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("estado", new TableInfo.Column("estado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(app.regate.models.account.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(9);
        _columnsMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("grupo_id", new TableInfo.Column("grupo_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("data", new TableInfo.Column("data", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("type_message", new TableInfo.Column("type_message", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("reply_to", new TableInfo.Column("reply_to", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("sended", new TableInfo.Column("sended", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(app.regate.models.Message).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsProfiles = new HashMap<String, TableInfo.Column>(8);
        _columnsProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("user_id", new TableInfo.Column("user_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("profile_photo", new TableInfo.Column("profile_photo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("apellido", new TableInfo.Column("apellido", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfiles = new TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles, _indicesProfiles);
        final TableInfo _existingProfiles = TableInfo.read(db, "profiles");
        if (!_infoProfiles.equals(_existingProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "profiles(app.regate.models.Profile).\n"
                  + " Expected:\n" + _infoProfiles + "\n"
                  + " Found:\n" + _existingProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(5);
        _columnsSettings.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("paid_type", new TableInfo.Column("paid_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("establecimiento_id", new TableInfo.Column("establecimiento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("payment_for_reservation", new TableInfo.Column("payment_for_reservation", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("horario_interval", new TableInfo.Column("horario_interval", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSettings.add(new TableInfo.ForeignKey("establecimientos", "CASCADE", "CASCADE", Arrays.asList("establecimiento_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(1);
        _indicesSettings.add(new TableInfo.Index("index_settings_establecimiento_id", false, Arrays.asList("establecimiento_id"), Arrays.asList("ASC")));
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(app.regate.models.Setting).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsLabels = new HashMap<String, TableInfo.Column>(4);
        _columnsLabels.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLabels.put("name", new TableInfo.Column("name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLabels.put("thumbnail", new TableInfo.Column("thumbnail", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLabels.put("type_label", new TableInfo.Column("type_label", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLabels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLabels = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLabels = new TableInfo("labels", _columnsLabels, _foreignKeysLabels, _indicesLabels);
        final TableInfo _existingLabels = TableInfo.read(db, "labels");
        if (!_infoLabels.equals(_existingLabels)) {
          return new RoomOpenHelper.ValidationResult(false, "labels(app.regate.models.Labels).\n"
                  + " Expected:\n" + _infoLabels + "\n"
                  + " Found:\n" + _existingLabels);
        }
        final HashMap<String, TableInfo.Column> _columnsGrupos = new HashMap<String, TableInfo.Column>(12);
        _columnsGrupos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("photo", new TableInfo.Column("photo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("is_visible", new TableInfo.Column("is_visible", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("visibility", new TableInfo.Column("visibility", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("last_message", new TableInfo.Column("last_message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("last_message_created", new TableInfo.Column("last_message_created", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrupos.put("messages_count", new TableInfo.Column("messages_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGrupos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGrupos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGrupos = new TableInfo("grupos", _columnsGrupos, _foreignKeysGrupos, _indicesGrupos);
        final TableInfo _existingGrupos = TableInfo.read(db, "grupos");
        if (!_infoGrupos.equals(_existingGrupos)) {
          return new RoomOpenHelper.ValidationResult(false, "grupos(app.regate.models.Grupo).\n"
                  + " Expected:\n" + _infoGrupos + "\n"
                  + " Found:\n" + _existingGrupos);
        }
        final HashMap<String, TableInfo.Column> _columnsUserGrupo = new HashMap<String, TableInfo.Column>(4);
        _columnsUserGrupo.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserGrupo.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserGrupo.put("grupo_id", new TableInfo.Column("grupo_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserGrupo.put("is_admin", new TableInfo.Column("is_admin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserGrupo = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysUserGrupo.add(new TableInfo.ForeignKey("grupos", "CASCADE", "CASCADE", Arrays.asList("grupo_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesUserGrupo = new HashSet<TableInfo.Index>(1);
        _indicesUserGrupo.add(new TableInfo.Index("index_user_grupo_grupo_id", false, Arrays.asList("grupo_id"), Arrays.asList("ASC")));
        final TableInfo _infoUserGrupo = new TableInfo("user_grupo", _columnsUserGrupo, _foreignKeysUserGrupo, _indicesUserGrupo);
        final TableInfo _existingUserGrupo = TableInfo.read(db, "user_grupo");
        if (!_infoUserGrupo.equals(_existingUserGrupo)) {
          return new RoomOpenHelper.ValidationResult(false, "user_grupo(app.regate.models.UserGrupo).\n"
                  + " Expected:\n" + _infoUserGrupo + "\n"
                  + " Found:\n" + _existingUserGrupo);
        }
        final HashMap<String, TableInfo.Column> _columnsMyGroups = new HashMap<String, TableInfo.Column>(3);
        _columnsMyGroups.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMyGroups.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMyGroups.put("request_estado", new TableInfo.Column("request_estado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMyGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMyGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMyGroups = new TableInfo("my_groups", _columnsMyGroups, _foreignKeysMyGroups, _indicesMyGroups);
        final TableInfo _existingMyGroups = TableInfo.read(db, "my_groups");
        if (!_infoMyGroups.equals(_existingMyGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "my_groups(app.regate.models.MyGroups).\n"
                  + " Expected:\n" + _infoMyGroups + "\n"
                  + " Found:\n" + _existingMyGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoriteEstablecimiento = new HashMap<String, TableInfo.Column>(2);
        _columnsFavoriteEstablecimiento.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEstablecimiento.put("establecimiento_id", new TableInfo.Column("establecimiento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteEstablecimiento = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteEstablecimiento = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteEstablecimiento = new TableInfo("favorite_establecimiento", _columnsFavoriteEstablecimiento, _foreignKeysFavoriteEstablecimiento, _indicesFavoriteEstablecimiento);
        final TableInfo _existingFavoriteEstablecimiento = TableInfo.read(db, "favorite_establecimiento");
        if (!_infoFavoriteEstablecimiento.equals(_existingFavoriteEstablecimiento)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_establecimiento(app.regate.models.FavoriteEstablecimiento).\n"
                  + " Expected:\n" + _infoFavoriteEstablecimiento + "\n"
                  + " Found:\n" + _existingFavoriteEstablecimiento);
        }
        final HashMap<String, TableInfo.Column> _columnsMessageInbox = new HashMap<String, TableInfo.Column>(7);
        _columnsMessageInbox.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("conversation_id", new TableInfo.Column("conversation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("sender_id", new TableInfo.Column("sender_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("reply_to", new TableInfo.Column("reply_to", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageInbox.put("sended", new TableInfo.Column("sended", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessageInbox = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessageInbox = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessageInbox = new TableInfo("message_inbox", _columnsMessageInbox, _foreignKeysMessageInbox, _indicesMessageInbox);
        final TableInfo _existingMessageInbox = TableInfo.read(db, "message_inbox");
        if (!_infoMessageInbox.equals(_existingMessageInbox)) {
          return new RoomOpenHelper.ValidationResult(false, "message_inbox(app.regate.models.MessageInbox).\n"
                  + " Expected:\n" + _infoMessageInbox + "\n"
                  + " Found:\n" + _existingMessageInbox);
        }
        final HashMap<String, TableInfo.Column> _columnsReservas = new HashMap<String, TableInfo.Column>(10);
        _columnsReservas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("instalacion_id", new TableInfo.Column("instalacion_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("instalacion_name", new TableInfo.Column("instalacion_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("establecimiento_id", new TableInfo.Column("establecimiento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("pagado", new TableInfo.Column("pagado", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("total_price", new TableInfo.Column("total_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("start_date", new TableInfo.Column("start_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("end_date", new TableInfo.Column("end_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservas.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReservas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReservas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReservas = new TableInfo("reservas", _columnsReservas, _foreignKeysReservas, _indicesReservas);
        final TableInfo _existingReservas = TableInfo.read(db, "reservas");
        if (!_infoReservas.equals(_existingReservas)) {
          return new RoomOpenHelper.ValidationResult(false, "reservas(app.regate.models.Reserva).\n"
                  + " Expected:\n" + _infoReservas + "\n"
                  + " Found:\n" + _existingReservas);
        }
        final HashMap<String, TableInfo.Column> _columnsNotification = new HashMap<String, TableInfo.Column>(7);
        _columnsNotification.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("entityId", new TableInfo.Column("entityId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("typeEntity", new TableInfo.Column("typeEntity", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("read", new TableInfo.Column("read", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotification = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotification = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotification = new TableInfo("notification", _columnsNotification, _foreignKeysNotification, _indicesNotification);
        final TableInfo _existingNotification = TableInfo.read(db, "notification");
        if (!_infoNotification.equals(_existingNotification)) {
          return new RoomOpenHelper.ValidationResult(false, "notification(app.regate.models.Notification).\n"
                  + " Expected:\n" + _infoNotification + "\n"
                  + " Found:\n" + _existingNotification);
        }
        final HashMap<String, TableInfo.Column> _columnsMessageSala = new HashMap<String, TableInfo.Column>(7);
        _columnsMessageSala.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("sala_id", new TableInfo.Column("sala_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("reply_to", new TableInfo.Column("reply_to", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageSala.put("sended", new TableInfo.Column("sended", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessageSala = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessageSala = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessageSala = new TableInfo("message_sala", _columnsMessageSala, _foreignKeysMessageSala, _indicesMessageSala);
        final TableInfo _existingMessageSala = TableInfo.read(db, "message_sala");
        if (!_infoMessageSala.equals(_existingMessageSala)) {
          return new RoomOpenHelper.ValidationResult(false, "message_sala(app.regate.models.MessageSala).\n"
                  + " Expected:\n" + _infoMessageSala + "\n"
                  + " Found:\n" + _existingMessageSala);
        }
        final HashMap<String, TableInfo.Column> _columnsSearchHistory = new HashMap<String, TableInfo.Column>(3);
        _columnsSearchHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("query", new TableInfo.Column("query", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSearchHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSearchHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSearchHistory = new TableInfo("search_history", _columnsSearchHistory, _foreignKeysSearchHistory, _indicesSearchHistory);
        final TableInfo _existingSearchHistory = TableInfo.read(db, "search_history");
        if (!_infoSearchHistory.equals(_existingSearchHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "search_history(app.regate.models.SearchHistory).\n"
                  + " Expected:\n" + _infoSearchHistory + "\n"
                  + " Found:\n" + _existingSearchHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsEmoji = new HashMap<String, TableInfo.Column>(4);
        _columnsEmoji.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmoji.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmoji.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmoji.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmoji = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmoji = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmoji = new TableInfo("emoji", _columnsEmoji, _foreignKeysEmoji, _indicesEmoji);
        final TableInfo _existingEmoji = TableInfo.read(db, "emoji");
        if (!_infoEmoji.equals(_existingEmoji)) {
          return new RoomOpenHelper.ValidationResult(false, "emoji(app.regate.models.Emoji).\n"
                  + " Expected:\n" + _infoEmoji + "\n"
                  + " Found:\n" + _existingEmoji);
        }
        final HashMap<String, TableInfo.Column> _columnsAttentionSchedule = new HashMap<String, TableInfo.Column>(6);
        _columnsAttentionSchedule.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttentionSchedule.put("day_week", new TableInfo.Column("day_week", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttentionSchedule.put("establecimiento_id", new TableInfo.Column("establecimiento_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttentionSchedule.put("open", new TableInfo.Column("open", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttentionSchedule.put("closed", new TableInfo.Column("closed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttentionSchedule.put("schedule_interval", new TableInfo.Column("schedule_interval", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttentionSchedule = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAttentionSchedule.add(new TableInfo.ForeignKey("establecimientos", "CASCADE", "CASCADE", Arrays.asList("establecimiento_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAttentionSchedule = new HashSet<TableInfo.Index>(1);
        _indicesAttentionSchedule.add(new TableInfo.Index("index_attention_schedule_establecimiento_id", false, Arrays.asList("establecimiento_id"), Arrays.asList("ASC")));
        final TableInfo _infoAttentionSchedule = new TableInfo("attention_schedule", _columnsAttentionSchedule, _foreignKeysAttentionSchedule, _indicesAttentionSchedule);
        final TableInfo _existingAttentionSchedule = TableInfo.read(db, "attention_schedule");
        if (!_infoAttentionSchedule.equals(_existingAttentionSchedule)) {
          return new RoomOpenHelper.ValidationResult(false, "attention_schedule(app.regate.models.AttentionSchedule).\n"
                  + " Expected:\n" + _infoAttentionSchedule + "\n"
                  + " Found:\n" + _existingAttentionSchedule);
        }
        final HashMap<String, TableInfo.Column> _columnsUserRoom = new HashMap<String, TableInfo.Column>(4);
        _columnsUserRoom.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRoom.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRoom.put("entity_id", new TableInfo.Column("entity_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserRoom.put("is_admin", new TableInfo.Column("is_admin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserRoom = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserRoom = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserRoom = new TableInfo("user_room", _columnsUserRoom, _foreignKeysUserRoom, _indicesUserRoom);
        final TableInfo _existingUserRoom = TableInfo.read(db, "user_room");
        if (!_infoUserRoom.equals(_existingUserRoom)) {
          return new RoomOpenHelper.ValidationResult(false, "user_room(app.regate.models.UserRoom).\n"
                  + " Expected:\n" + _infoUserRoom + "\n"
                  + " Found:\n" + _existingUserRoom);
        }
        final HashMap<String, TableInfo.Column> _columnsUserBalance = new HashMap<String, TableInfo.Column>(3);
        _columnsUserBalance.put("balance_id", new TableInfo.Column("balance_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserBalance.put("profile_id", new TableInfo.Column("profile_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserBalance.put("coins", new TableInfo.Column("coins", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserBalance = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserBalance = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserBalance = new TableInfo("user_balance", _columnsUserBalance, _foreignKeysUserBalance, _indicesUserBalance);
        final TableInfo _existingUserBalance = TableInfo.read(db, "user_balance");
        if (!_infoUserBalance.equals(_existingUserBalance)) {
          return new RoomOpenHelper.ValidationResult(false, "user_balance(app.regate.models.account.UserBalance).\n"
                  + " Expected:\n" + _infoUserBalance + "\n"
                  + " Found:\n" + _existingUserBalance);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ea0e1278321de7b33d516f9b237cbe0d", "b259095f36c76429133f3242e38656a6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "establecimientos","instalaciones","cupos","users","messages","profiles","settings","labels","grupos","user_grupo","my_groups","favorite_establecimiento","message_inbox","reservas","notification","message_sala","search_history","emoji","attention_schedule","user_room","user_balance");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `establecimientos`");
      _db.execSQL("DELETE FROM `instalaciones`");
      _db.execSQL("DELETE FROM `cupos`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `profiles`");
      _db.execSQL("DELETE FROM `settings`");
      _db.execSQL("DELETE FROM `labels`");
      _db.execSQL("DELETE FROM `grupos`");
      _db.execSQL("DELETE FROM `user_grupo`");
      _db.execSQL("DELETE FROM `my_groups`");
      _db.execSQL("DELETE FROM `favorite_establecimiento`");
      _db.execSQL("DELETE FROM `message_inbox`");
      _db.execSQL("DELETE FROM `reservas`");
      _db.execSQL("DELETE FROM `notification`");
      _db.execSQL("DELETE FROM `message_sala`");
      _db.execSQL("DELETE FROM `search_history`");
      _db.execSQL("DELETE FROM `emoji`");
      _db.execSQL("DELETE FROM `attention_schedule`");
      _db.execSQL("DELETE FROM `user_room`");
      _db.execSQL("DELETE FROM `user_balance`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RoomEstablecimientoDao.class, RoomEstablecimientoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomInstalacionDao.class, RoomInstalacionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomCupoDao.class, RoomCupoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomUserDao.class, RoomUserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomProfileDao.class, RoomProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomMessageProfileDao.class, RoomMessageProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomLabelDao.class, RoomLabelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomGrupoDao.class, RoomGrupoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomUserGrupoDao.class, RoomUserGrupoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomUserRoomDao.class, RoomUserRoomDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomMyGroupsDao.class, RoomMyGroupsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomFavoriteEstablecimientoDao.class, RoomFavoriteEstablecimientoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomMessageInboxDao.class, RoomMessageInboxDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomReservaDao.class, RoomReservaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomNotificationDao.class, RoomNotificationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomMessageSalaDao.class, RoomMessageSalaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomSearchHistoryDao.class, RoomSearchHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomEmojiDao.class, RoomEmojiDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RoomEstablecimientoDao establecimientoDao() {
    if (_roomEstablecimientoDao != null) {
      return _roomEstablecimientoDao;
    } else {
      synchronized(this) {
        if(_roomEstablecimientoDao == null) {
          _roomEstablecimientoDao = new RoomEstablecimientoDao_Impl(this);
        }
        return _roomEstablecimientoDao;
      }
    }
  }

  @Override
  public RoomInstalacionDao instalacionDao() {
    if (_roomInstalacionDao != null) {
      return _roomInstalacionDao;
    } else {
      synchronized(this) {
        if(_roomInstalacionDao == null) {
          _roomInstalacionDao = new RoomInstalacionDao_Impl(this);
        }
        return _roomInstalacionDao;
      }
    }
  }

  @Override
  public RoomCupoDao cupoDao() {
    if (_roomCupoDao != null) {
      return _roomCupoDao;
    } else {
      synchronized(this) {
        if(_roomCupoDao == null) {
          _roomCupoDao = new RoomCupoDao_Impl(this);
        }
        return _roomCupoDao;
      }
    }
  }

  @Override
  public RoomUserDao userDao() {
    if (_roomUserDao != null) {
      return _roomUserDao;
    } else {
      synchronized(this) {
        if(_roomUserDao == null) {
          _roomUserDao = new RoomUserDao_Impl(this);
        }
        return _roomUserDao;
      }
    }
  }

  @Override
  public RoomProfileDao profileDao() {
    if (_roomProfileDao != null) {
      return _roomProfileDao;
    } else {
      synchronized(this) {
        if(_roomProfileDao == null) {
          _roomProfileDao = new RoomProfileDao_Impl(this);
        }
        return _roomProfileDao;
      }
    }
  }

  @Override
  public RoomMessageProfileDao messageProfileDao() {
    if (_roomMessageProfileDao != null) {
      return _roomMessageProfileDao;
    } else {
      synchronized(this) {
        if(_roomMessageProfileDao == null) {
          _roomMessageProfileDao = new RoomMessageProfileDao_Impl(this);
        }
        return _roomMessageProfileDao;
      }
    }
  }

  @Override
  public RoomLabelDao labelsDao() {
    if (_roomLabelDao != null) {
      return _roomLabelDao;
    } else {
      synchronized(this) {
        if(_roomLabelDao == null) {
          _roomLabelDao = new RoomLabelDao_Impl(this);
        }
        return _roomLabelDao;
      }
    }
  }

  @Override
  public RoomGrupoDao grupoDao() {
    if (_roomGrupoDao != null) {
      return _roomGrupoDao;
    } else {
      synchronized(this) {
        if(_roomGrupoDao == null) {
          _roomGrupoDao = new RoomGrupoDao_Impl(this);
        }
        return _roomGrupoDao;
      }
    }
  }

  @Override
  public RoomUserGrupoDao userGrupoDao() {
    if (_roomUserGrupoDao != null) {
      return _roomUserGrupoDao;
    } else {
      synchronized(this) {
        if(_roomUserGrupoDao == null) {
          _roomUserGrupoDao = new RoomUserGrupoDao_Impl(this);
        }
        return _roomUserGrupoDao;
      }
    }
  }

  @Override
  public RoomUserRoomDao userRoomDao() {
    if (_roomUserRoomDao != null) {
      return _roomUserRoomDao;
    } else {
      synchronized(this) {
        if(_roomUserRoomDao == null) {
          _roomUserRoomDao = new RoomUserRoomDao_Impl(this);
        }
        return _roomUserRoomDao;
      }
    }
  }

  @Override
  public RoomMyGroupsDao myGroupsDao() {
    if (_roomMyGroupsDao != null) {
      return _roomMyGroupsDao;
    } else {
      synchronized(this) {
        if(_roomMyGroupsDao == null) {
          _roomMyGroupsDao = new RoomMyGroupsDao_Impl(this);
        }
        return _roomMyGroupsDao;
      }
    }
  }

  @Override
  public RoomFavoriteEstablecimientoDao favoriteEstablecimientos() {
    if (_roomFavoriteEstablecimientoDao != null) {
      return _roomFavoriteEstablecimientoDao;
    } else {
      synchronized(this) {
        if(_roomFavoriteEstablecimientoDao == null) {
          _roomFavoriteEstablecimientoDao = new RoomFavoriteEstablecimientoDao_Impl(this);
        }
        return _roomFavoriteEstablecimientoDao;
      }
    }
  }

  @Override
  public RoomMessageInboxDao messageInboxDao() {
    if (_roomMessageInboxDao != null) {
      return _roomMessageInboxDao;
    } else {
      synchronized(this) {
        if(_roomMessageInboxDao == null) {
          _roomMessageInboxDao = new RoomMessageInboxDao_Impl(this);
        }
        return _roomMessageInboxDao;
      }
    }
  }

  @Override
  public RoomReservaDao reservaDao() {
    if (_roomReservaDao != null) {
      return _roomReservaDao;
    } else {
      synchronized(this) {
        if(_roomReservaDao == null) {
          _roomReservaDao = new RoomReservaDao_Impl(this);
        }
        return _roomReservaDao;
      }
    }
  }

  @Override
  public RoomNotificationDao notificationDao() {
    if (_roomNotificationDao != null) {
      return _roomNotificationDao;
    } else {
      synchronized(this) {
        if(_roomNotificationDao == null) {
          _roomNotificationDao = new RoomNotificationDao_Impl(this);
        }
        return _roomNotificationDao;
      }
    }
  }

  @Override
  public RoomMessageSalaDao messageSalaDao() {
    if (_roomMessageSalaDao != null) {
      return _roomMessageSalaDao;
    } else {
      synchronized(this) {
        if(_roomMessageSalaDao == null) {
          _roomMessageSalaDao = new RoomMessageSalaDao_Impl(this);
        }
        return _roomMessageSalaDao;
      }
    }
  }

  @Override
  public RoomSearchHistoryDao searchHistoryDao() {
    if (_roomSearchHistoryDao != null) {
      return _roomSearchHistoryDao;
    } else {
      synchronized(this) {
        if(_roomSearchHistoryDao == null) {
          _roomSearchHistoryDao = new RoomSearchHistoryDao_Impl(this);
        }
        return _roomSearchHistoryDao;
      }
    }
  }

  @Override
  public RoomEmojiDao emojiDao() {
    if (_roomEmojiDao != null) {
      return _roomEmojiDao;
    } else {
      synchronized(this) {
        if(_roomEmojiDao == null) {
          _roomEmojiDao = new RoomEmojiDao_Impl(this);
        }
        return _roomEmojiDao;
      }
    }
  }
}
