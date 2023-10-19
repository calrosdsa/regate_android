package app.regate.data.sala

import me.tatarka.inject.annotations.Provides

interface SalaBinds {
    @Provides
    fun provideSalaDataSource(
        bind: SalaDataSourceImpl
    ): SalaDataSource = bind
}