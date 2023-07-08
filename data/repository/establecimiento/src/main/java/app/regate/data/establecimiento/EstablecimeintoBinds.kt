package app.regate.data.establecimiento

import me.tatarka.inject.annotations.Provides

interface EstablecimeintoBinds {
    @Provides
    fun provideEstablecimientoDataSource(
        bind: EstablecimientoDataSourceImpl
    ): EstablecimientoDataSource = bind
}