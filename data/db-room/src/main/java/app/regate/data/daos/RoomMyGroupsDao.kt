package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.GrupoWithMessage
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomMyGroupsDao:RoomEntityDao<MyGroups>,MyGroupsDao {
//    @Query("select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id")
//    abstract override fun observeUserGroups(): Flow<List<Grupo>>

//        select * from grupos as g (select count(*) from messages as m where g.id = m.grupo_id ) as cotun
    @Transaction
    @Query("""
        select g.*,(select count(*) from messages as m where g.id = m.grupo_id ) as local_count_messages
        from my_groups as ug inner join grupos as g on g.id = ug.group_id
        """)
    abstract override fun observeUserGroupsWithMessage(): Flow<List<GrupoWithMessage>>
    @Transaction
    @Query("""
        select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id
        """)
    abstract override fun observeUserGroups(): Flow<List<Grupo>>

    @Query("delete from my_groups")
    abstract override fun deleteAll()
}