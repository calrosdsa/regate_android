package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.models.UserRoom
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserRoomDao:RoomEntityDao<UserRoom>,UserRoomDao {
//    @Transaction
//    @Query(
//        """
//        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.is_out,ug.id as id,
//        ug.sala_id as parent_id,(1) as type_entity from user_room as ug
//        inner join profiles as p on p.id = ug.profile_id
//        where ug.sala_id = :id
//    """
//    )
//    abstract override fun observeUsersRoom(id: Long): Flow<List<UserProfileGrupoAndSalaDto>>

    @Query("select * from user_room where sala_id = :salaId and profile_id = :profileId")
    abstract override suspend fun getUserRoom(salaId: Long, profileId: Long): UserRoom?
    @Query("update user_room set is_out = :isOut where id = :id")
    abstract override suspend fun updateUserIsOut(id: Long, isOut: Boolean)


    @Query("delete from user_room where id = :id")
    abstract override suspend fun deleteUserRoom(id: Long)
}