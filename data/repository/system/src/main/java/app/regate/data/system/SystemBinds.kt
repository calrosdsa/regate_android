package app.regate.data.system

import me.tatarka.inject.annotations.Provides

interface SystemBinds {
    @Provides
    fun provideLabelDataSource(
        bind: SystemDataSourceImpl
    ): SystemDataSource = bind
}