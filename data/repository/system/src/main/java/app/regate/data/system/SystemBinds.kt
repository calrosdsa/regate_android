package app.regate.data.system

import me.tatarka.inject.annotations.Provides

interface SystemBinds {
    @Provides
    fun provideSystemDataSource(
        bind: SystemDataSourceImpl
    ): SystemDataSource = bind
}