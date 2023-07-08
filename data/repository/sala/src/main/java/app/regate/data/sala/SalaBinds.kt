package app.regate.data.sala

import me.tatarka.inject.annotations.Provides

interface SalaBinds {
    @Provides
    fun provideInstalacionDataSource(
        bind: SalaDataSourceImpl
    ): SalaDataSource = bind
}