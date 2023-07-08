/*
 * Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.regate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.LabelDao
import app.regate.data.daos.RoomCupoDao
import app.regate.data.daos.RoomEstablecimientoDao
import app.regate.data.daos.RoomGrupoDao
import app.regate.data.daos.RoomInstalacionDao
import app.regate.data.daos.RoomLabelDao
import app.regate.data.daos.RoomMessageProfileDao
import app.regate.data.daos.RoomProfileDao
import app.regate.data.daos.RoomUserDao
import app.regate.data.daos.RoomUserGrupoDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.db.AppDatabase
import app.regate.data.db.AppTypeConverters
import app.regate.data.db.DateTimeTypeConverters
import app.regate.models.Cupo
import app.regate.models.Establecimiento
import app.regate.models.Grupo
import app.regate.models.Instalacion
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.models.Message
import app.regate.models.Profile
import app.regate.models.Setting
import app.regate.models.User
import app.regate.models.UserGrupo

@Database(
    entities = [
        Establecimiento::class,
        Instalacion::class,
        Cupo::class,
        User::class,
        Message::class,
        Profile::class,
        Setting::class,
        Labels::class,
        Grupo::class,
        UserGrupo::class
    ],
//    views = [
//        ShowsWatchStats::class,
//        ShowsLastWatched::class,
//        ShowsNextToWatch::class,
//    ],
    version = 3,
//    autoMigrations = [
//        AutoMigration(from = 24, to = 25),
//        AutoMigration(from = 25, to = 26),
//        AutoMigration(from = 26, to = 27),
//        AutoMigration(from = 27, to = 28), // can remove this later
//        AutoMigration(from = 28, to = 29), // can remove this later
//        AutoMigration(from = 29, to = 30), // can remove this later
//        AutoMigration(from = 27, to = 30),
//        AutoMigration(from = 30, to = 31, spec = AppRoomDatabase.AutoMigrationSpec31::class),
//        AutoMigration(from = 31, to = 32),
//    ],
)
@TypeConverters(AppTypeConverters::class,DateTimeTypeConverters::class)
abstract class AppRoomDatabase : RoomDatabase(), AppDatabase {

//    @DeleteColumn.Entries(
//        DeleteColumn(tableName = "shows", columnName = "last_trakt_data_update"),
//    )
//    class AutoMigrationSpec31 : AutoMigrationSpec
    abstract override fun establecimientoDao(): RoomEstablecimientoDao
    abstract override fun instalacionDao(): RoomInstalacionDao
    abstract override fun cupoDao(): RoomCupoDao
    abstract override fun userDao(): RoomUserDao
    abstract override fun profileDao(): RoomProfileDao
    abstract  override fun messageProfileDao():RoomMessageProfileDao
    abstract override fun labelsDao(): RoomLabelDao
    abstract override fun grupoDao(): RoomGrupoDao
    abstract override fun userGrupoDao(): RoomUserGrupoDao
}
