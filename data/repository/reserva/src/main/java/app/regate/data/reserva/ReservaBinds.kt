package app.regate.data.reserva

import me.tatarka.inject.annotations.Provides

interface ReservaBinds {
    @Provides
    fun provideReservaDataSource(
        bind: ReservaDataSourceImpl
    ): ReservaDataSource = bind
}