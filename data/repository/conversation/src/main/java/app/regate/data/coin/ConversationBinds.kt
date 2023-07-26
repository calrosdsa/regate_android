package app.regate.data.coin

import me.tatarka.inject.annotations.Provides

interface ConversationBinds {
    @Provides
    fun provideLabelDataSource(
        bind: ConversationDataSourceImpl
    ): ConversationDataSource = bind
}