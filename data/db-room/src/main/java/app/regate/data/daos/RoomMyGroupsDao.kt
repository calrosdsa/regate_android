package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class RoomMyGroupsDao:RoomEntityDao<MyGroups>,MyGroupsDao {
//    @Query("select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id")
//    abstract override fun observeUserGroups(): Flow<List<Grupo>>

//        select * from grupos as g (select count(*) from messages as m where g.id = m.grupo_id ) as cotun
    @Transaction
    @Query("""
        select g.*,ug.last_message,ug.last_message_created,ug.messages_count,(select count(*) from messages as m where g.id = m.grupo_id ) as local_count_messages
        from my_groups as ug inner join grupos as g on g.id = ug.group_id
        where request_estado = :requestEstado
        """)
    abstract override fun observeUserGroupsWithMessage(requestEstado:Int): Flow<List<GrupoWithMessage>>
    @Transaction
    @Query("""
        select g.* from my_groups as ug inner join grupos as g on g.id = ug.group_id
        """)
    abstract override fun observeUserGroups(): Flow<List<Grupo>>
    @Transaction
    @Query("select * from my_groups")
    abstract override fun observeMyGroups(): Flow<List<MyGroups>>
    @Transaction
    @Query("select * from my_groups where group_id = :grupoId")
    abstract override fun observeMyGroupById(grupoId:Long): Flow<MyGroups?>
    @Query("delete from  my_groups where request_estado = :estado")
    abstract override suspend fun deleteMyGroups(estado:Int)
    @Query("delete from my_groups")
    abstract override suspend fun deleteAll()
    @Query("delete from my_groups where group_id = :id")
    abstract override suspend fun deleteByGroupId(id: Long)

    @Query("""
        update my_groups set last_message_created = :created,
         last_message = :message,
         messages_count = messages_count +1
         where group_id = :grupoId
         """)
    abstract override suspend fun updateLastMessageGrupo(
        grupoId: Long,
        message: String,
        created: Instant
    )
}