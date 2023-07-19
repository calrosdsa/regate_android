package app.regate.data.daos

import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow

interface MyGroupsDao:EntityDao<MyGroups> {
    fun observeUserGroups(): Flow<List<Grupo>>
    fun deleteAll()
}