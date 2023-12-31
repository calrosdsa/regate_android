package app.regate.data.daos

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.models.grupo.UserGrupo
import kotlinx.coroutines.flow.Flow

interface UserGrupoDao:EntityDao<UserGrupo> {
    fun observeUsersGrupo(id:Long):Flow<List<UserProfileGrupoAndSalaDto>>
    suspend fun getUserGroup(groupId:Long, profileId:Long): UserGrupo?
    suspend fun deleteUsers(id:Long)
    suspend fun deleteUserGroup(id:Long)
    suspend fun deleteUsersGroup(groupId:Long)
    suspend fun updateUser(id:Long,status:Boolean)
    suspend fun updateUserIsOut(id:Long,isOut: Boolean)

    fun observeUsersRoom(id:Long): Flow<List<UserProfileGrupoAndSalaDto>>
}