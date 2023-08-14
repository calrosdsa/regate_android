package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.UserGrupo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserGrupoDao:UserGrupoDao,RoomEntityDao<UserGrupo> {
    @Transaction
    @Query("""
        select p.id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.id as user_group_id from user_grupo as ug
        inner join profiles as p on p.id = ug.profile_id
        where ug.grupo_id = :id
    """)
    abstract override fun observeUsersGrupo(id: Long): Flow<List<UserProfileGrupo>>

    @Query("DELETE FROM user_grupo where grupo_id = :id")
    abstract override suspend fun deleteUsers(id:Long)

    @Query("DELETE FROM user_grupo where  grupo_id = :groupId")
    abstract override suspend fun deleteUsersGroup(groupId:Long)
    @Query("DELETE FROM user_grupo where  id = :id")
    abstract override suspend fun deleteUserGroup(id: Long)

    @Query("UPDATE user_grupo set is_admin = :status where id = :id")
    abstract override suspend fun updateUser(id: Long, status: Boolean)
}