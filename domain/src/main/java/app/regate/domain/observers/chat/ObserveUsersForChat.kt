package app.regate.domain.observers.chat

import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.daos.UserGrupoDao
import app.regate.data.dto.chat.TypeChat
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersForChat (
    private val usersGrupoDao: UserGrupoDao
    ):SubjectInteractor<ObserveUsersForChat.Params,List<UserProfileGrupoAndSala>>(){

        override fun createObservable(params: Params): Flow<List<UserProfileGrupoAndSala>> {
            return when(params.typeChat){
                TypeChat.TYPE_CHAT_GRUPO.ordinal -> usersGrupoDao.observeUsersGrupo(params.id)
                TypeChat.TYPE_CHAT_SALA.ordinal -> usersGrupoDao.observeUsersRoom(params.id)
                else -> flowOf(emptyList())
            }

        }

        data class Params(
            val id:Long,
            val typeChat:Int
        )
}