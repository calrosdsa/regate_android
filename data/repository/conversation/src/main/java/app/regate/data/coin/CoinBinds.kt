package app.regate.data.coin

import me.tatarka.inject.annotations.Provides

interface CoinBinds {
    @Provides
    fun provideLabelDataSource(
        bind: CoinDataSourceImpl
    ): CoinDataSource = bind
}