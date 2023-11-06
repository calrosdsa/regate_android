package app.regate.data.daos

import app.regate.models.grupo.Grupo
import app.regate.models.grupo.MyGroups
import kotlinx.coroutines.flow.Flow

interface MyGroupsDao:EntityDao<MyGroups> {
    fun observeUserGroups(): Flow<List<Grupo>>
    fun observeMyGroups(): Flow<List<MyGroups>>
    fun observeMyGroupById(grupoId:Long): Flow<MyGroups?>
    suspend fun deleteMyGroups(estado:Int)
    suspend fun deleteAll()
    suspend fun deleteByGroupId(id:Long)
}