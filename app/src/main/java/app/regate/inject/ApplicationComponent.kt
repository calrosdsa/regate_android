/*
 * Copyright 2023 Google LLC
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

package app.regate.inject

import android.app.Application
import android.content.Context
import app.regate.RegateApplication
import app.regate.appinitializers.AppInitializers
import app.regate.data.RoomDatabaseComponent
import app.regate.data.account.AccountBinds
import app.regate.data.establecimiento.EstablecimeintoBinds
import app.regate.data.grupo.GrupoBinds
import app.regate.data.instalacion.InstalacionBinds
import app.regate.data.labels.LabelsBinds
import app.regate.data.reserva.ReservaBinds
import app.regate.data.sala.SalaBinds
import app.regate.data.users.UsersBinds
import app.regate.settings.PreferencesComponent
import app.regate.tasks.AppWorkerFactory
import app.regate.tasks.TasksComponent
//import app.regate.util.LoggerComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class ApplicationComponent(
    @get:Provides val application: Application,
):
    RoomDatabaseComponent,
    PreferencesComponent,
    AppComponent,
//    AuthComponent,
    NetworkComponent,
//    ApiComponent,
    AccountBinds,
    EstablecimeintoBinds,
    InstalacionBinds,
    ReservaBinds,
    SalaBinds,
    UsersBinds,
    TasksComponent,
    LabelsBinds,
    GrupoBinds
//    LoggerComponent
{
    abstract val initializers: AppInitializers
    abstract val workerFactory: AppWorkerFactory

    companion object {
        fun from(context: Context): ApplicationComponent {
            return (context.applicationContext as RegateApplication).component
        }
    }
}
