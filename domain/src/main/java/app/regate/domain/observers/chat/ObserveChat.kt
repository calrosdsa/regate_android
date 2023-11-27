package app.regate.domain.observers.chat

import app.regate.data.daos.ChatDao
import app.regate.data.instalacion.CupoRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Cupo
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveChat(
    private val chatDao: ChatDao
) :SubjectInteractor<ObserveChat.Params,Chat>(){

    override fun createObservable(params: Params): Flow<Chat> {
        return chatDao.observeChat(params.id)
    }

    data class Params(
        val id:Long,
    )
}