package app.regate.data.chat

import me.tatarka.inject.annotations.Provides

interface ConversationBinds {
    @Provides
    fun provideLabelDataSource(
        bind: ChatDataSourceImpl
    ): ChatDataSource = bind
}