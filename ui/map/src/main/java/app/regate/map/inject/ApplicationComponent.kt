package app.regate.map.inject

import android.app.Application
import android.content.Context
import app.regate.api.NetworkComponent
import app.regate.data.RoomDatabaseComponent
import app.regate.data.account.AccountBinds
import app.regate.data.establecimiento.EstablecimeintoBinds
import app.regate.data.instalacion.InstalacionBinds
import app.regate.data.reserva.ReservaBinds
import app.regate.data.sala.SalaBinds
import app.regate.data.users.UsersBinds
import app.regate.inject.ApplicationScope
import app.regate.settings.PreferencesComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class ApplicationComponent(
    @get:Provides val application: Application,
):
    RoomDatabaseComponent,
    NetworkComponent,
    PreferencesComponent,
    AccountBinds,
    EstablecimeintoBinds,
    InstalacionBinds,
    ReservaBinds,
    SalaBinds,
    UsersBinds
{
    companion object {
        private var instance: ApplicationComponent? = null

        /**
         * Get a singleton instance of [ApplicationComponent].
         */
        fun getInstance(context: Context) = instance ?: ApplicationComponent::class.create(
            context.applicationContext as Application
        ).also { instance = it }
    }
}