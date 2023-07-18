package app.regate.data.daos

import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.Grupo
import app.regate.models.UserGrupo
import kotlinx.coroutines.flow.Flow

interface UserGrupoDao:EntityDao<UserGrupo> {
    fun observeUsersGrupo(id:Long):Flow<List<UserProfileGrupo>>
    suspend fun deleteUsers(id:Long)
    suspend fun deleteUserGroup(id:Long)
    suspend fun updateUser(id:Long,status:Boolean)
}