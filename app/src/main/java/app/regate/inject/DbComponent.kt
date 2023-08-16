package app.regate.inject

import android.app.Application
import app.regate.data.RoomDatabaseComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class DbComponent (
    @get:Provides val application: Application,
):RoomDatabaseComponent