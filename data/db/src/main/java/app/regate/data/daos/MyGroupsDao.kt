package app.regate.data.daos

import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow

interface MyGroupsDao:EntityDao<MyGroups> {
    fun observeUserGroupsWithMessage(requestEstado: Int): Flow<List<GrupoWithMessage>>
    fun observeUserGroups(): Flow<List<Grupo>>
    fun observeMyGroups(): Flow<List<MyGroups>>
    fun observeMyGroupById(grupoId:Long): Flow<MyGroups?>
    suspend fun deleteMyGroups(estado:Int)
    suspend fun deleteAll()
    suspend fun deleteByGroupId(id:Long)
}