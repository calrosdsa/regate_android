package app.regate.data.account

import me.tatarka.inject.annotations.Provides

interface AccountBinds {
    @Provides
    fun provideAccountDataSource(
        bind:AccountDataSourceImpl
    ):AccountDataSource = bind
}