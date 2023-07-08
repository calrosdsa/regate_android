package app.regate.data.instalacion

import me.tatarka.inject.annotations.Provides

interface InstalacionBinds {
    @Provides
    fun provideInstalacionDataSource(
        bind: InstalacionDataSourceImpl
    ): InstalacionDataSource = bind
}