package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.UserRoom
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserRoomDao:RoomEntityDao<UserRoom>,UserRoomDao {
    @Transaction
    @Query(
        """
        select p.id as profile_id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.is_out,ug.id as id from user_room as ug
        inner join profiles as p on p.id = ug.profile_id
        where ug.sala_id = :id
    """
    )
    abstract override fun observeUsersRoom(id: Long): Flow<List<UserProfileGrupoAndSala>>

    @Query("select count(*) from user_room where sala_id = :roomId and is_out = :isOut ")
    abstract override suspend fun getUsersCount(isOut: Boolean, roomId: Long): Int
}