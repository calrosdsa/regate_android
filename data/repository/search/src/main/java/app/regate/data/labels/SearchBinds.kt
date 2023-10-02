package app.regate.data.labels

import me.tatarka.inject.annotations.Provides

interface SearchBinds {
    @Provides
    fun provideLabelDataSource(
        bind: SearchDataSourceImpl
    ): SearchDataSource = bind
}