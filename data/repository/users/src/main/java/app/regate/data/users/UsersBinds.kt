package app.regate.data.users

import me.tatarka.inject.annotations.Provides

interface UsersBinds {
    @Provides
    fun provideAccountDataSource(
        bind: UsersDataSourceImpl
    ): UsersDataSource = bind
}