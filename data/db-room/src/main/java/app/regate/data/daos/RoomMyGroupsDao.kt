package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomMyGroupsDao:RoomEntityDao<MyGroups>,MyGroupsDao {
    @Query("select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id")
    abstract override fun observeUserGroups(): Flow<List<Grupo>>

    @Query("delete from my_groups")
    abstract override fun deleteAll()
}