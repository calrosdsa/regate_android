package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.grupo.Grupo
import app.regate.models.grupo.MyGroups
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomMyGroupsDao:RoomEntityDao<MyGroups>,MyGroupsDao {
//    @Query("select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id")
//    abstract override fun observeUserGroups(): Flow<List<Grupo>>

//        select * from grupos as g (select count(*) from messages as m where g.id = m.grupo_id ) as cotun
    @Transaction
    @Query(
        """
        select g.* from my_groups as ug inner join grupos as g on g.id = ug.id
        """
    )
    abstract override fun observeUserGroups(): Flow<List<Grupo>>
    @Transaction
    @Query("select * from my_groups")
    abstract override fun observeMyGroups(): Flow<List<MyGroups>>
    @Transaction
    @Query("select * from my_groups where id = :grupoId")
    abstract override fun observeMyGroupById(grupoId:Long): Flow<MyGroups?>
    @Query("delete from  my_groups where request_estado = :estado")
    abstract override suspend fun deleteMyGroups(estado:Int)
    @Query("delete from my_groups")
    abstract override suspend fun deleteAll()
    @Query("delete from my_groups where id = :id")
    abstract override suspend fun deleteByGroupId(id: Long)
}