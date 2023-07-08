package app.regate.data.grupo

import me.tatarka.inject.annotations.Provides

interface GrupoBinds {
    @Provides
    fun provideInstalacionDataSource(
        bind: GrupoDataSourceImpl
    ): GrupoDataSource = bind
}