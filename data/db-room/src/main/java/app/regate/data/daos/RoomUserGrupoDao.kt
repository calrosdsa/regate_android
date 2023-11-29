package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.models.grupo.UserGrupo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserGrupoDao:UserGrupoDao,RoomEntityDao<UserGrupo> {
    @Transaction
    @Query("""
        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.is_out,ug.id as id,
        ug.grupo_id as parent_id,(2) as type_entity  from user_grupo as ug
        inner join profiles as p on p.id = ug.profile_id
        where ug.grupo_id = :id
    """)
    abstract override fun observeUsersGrupo(id: Long): Flow<List<UserProfileGrupoAndSalaDto>>

    @Transaction
    @Query("""
        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ur.is_admin,ur.is_out,ur.id as id, 
        ur.sala_id as parent_id,(1) as type_entity from user_room as ur
        inner join profiles as p on p.id = ur.profile_id
        where ur.sala_id = :id
    """)
    abstract override fun observeUsersRoom(id: Long): Flow<List<UserProfileGrupoAndSalaDto>>
    @Query("select count(*) from user_grupo where grupo_id = :grupoId and is_out = :isOut ")
    abstract override suspend fun getUsersCount(isOut: Boolean, grupoId: Long): Int
    @Query("DELETE FROM user_grupo where grupo_id = :id")
    abstract override suspend fun deleteUsers(id:Long)

    @Query("DELETE FROM user_grupo where  grupo_id = :groupId")
    abstract override suspend fun deleteUsersGroup(groupId:Long)
    @Query("DELETE FROM user_grupo where  id = :id")
    abstract override suspend fun deleteUserGroup(id: Long)

    @Query("UPDATE user_grupo set is_admin = :status where id = :id")
    abstract override suspend fun updateUser(id: Long, status: Boolean)
}