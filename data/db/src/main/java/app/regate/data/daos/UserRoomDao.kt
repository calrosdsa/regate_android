package app.regate.data.daos

import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.UserRoom
import kotlinx.coroutines.flow.Flow

interface UserRoomDao:EntityDao<UserRoom> {
    fun observeUsersRoom(id:Long): Flow<List<UserProfileGrupoAndSala>>
    suspend fun getUsersCount(isOut:Boolean,roomId:Long):Int

}