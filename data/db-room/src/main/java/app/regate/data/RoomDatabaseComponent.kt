/*
 * Copyright 2019 Google LLC
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

import android.app.Application
import android.os.Debug
import androidx.room.Room
import app.regate.data.daos.CupoDao
import app.regate.data.daos.EstablecimientoDao
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.InstalacionDao
import app.regate.data.daos.LabelDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.db.AppDatabase
import app.regate.data.db.DatabaseTransactionRunner
import app.regate.data.db.RoomTransactionRunner
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides

interface RoomDatabaseComponent {
    @ApplicationScope
    @Provides
    fun provideAppRoomDatabase(
        application: Application,
    ): AppRoomDatabase {
        val builder = Room.databaseBuilder(application, AppRoomDatabase::class.java, "regate.db")
            .fallbackToDestructiveMigration()
//        if (Debug.isDebuggerConnected()) {
//            builder.allowMainThreadQueries()
//        }
        return builder.build()
    }
    @Provides
    fun provideAppDatabase(bind: AppRoomDatabase): AppDatabase = bind
    @Provides
    fun provideAppLabelsDao(db:AppDatabase):LabelDao = db.labelsDao()
    @Provides
    fun provideAppShowDao(db: AppDatabase): EstablecimientoDao = db.establecimientoDao()
    @Provides
    fun provideAppInstalacionDao(db:AppDatabase): InstalacionDao = db.instalacionDao()
    @Provides
    fun provideAppCupoDao(db:AppDatabase):CupoDao = db.cupoDao()
    @Provides
    fun provideAppUserDao(db:AppDatabase):UserDao = db.userDao()
    @Provides
    fun provideMessageProfileDao(db:AppDatabase):MessageProfileDao = db.messageProfileDao()
    @Provides
    fun provideProfileDao(db:AppDatabase):ProfileDao = db.profileDao()
    @Provides
    fun provideGrupoDao(db:AppDatabase):GrupoDao = db.grupoDao()
    @Provides
    fun provideUserGrupoDao(db:AppDatabase):UserGrupoDao = db.userGrupoDao()

    @Provides
    fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner = runner
}
