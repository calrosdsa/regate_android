package app.regate.data.daos

import app.regate.compoundmodels.GrupoWithMessage
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow

interface MyGroupsDao:EntityDao<MyGroups> {
    fun observeUserGroupsWithMessage(): Flow<List<GrupoWithMessage>>
    fun observeUserGroups(): Flow<List<Grupo>>

    fun deleteAll()
}