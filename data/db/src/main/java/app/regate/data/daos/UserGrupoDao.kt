package app.regate.data.daos

import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.grupo.UserGrupo
import kotlinx.coroutines.flow.Flow

interface UserGrupoDao:EntityDao<UserGrupo> {
    fun observeUsersGrupo(id:Long):Flow<List<UserProfileGrupoAndSala>>
    suspend fun deleteUsers(id:Long)
    suspend fun deleteUserGroup(id:Long)
    suspend fun deleteUsersGroup(groupId:Long)
    suspend fun updateUser(id:Long,status:Boolean)
    suspend fun getUsersCount(isOut:Boolean,grupoId:Long):Int
    fun observeUsersRoom(id:Long): Flow<List<UserProfileGrupoAndSala>>
}