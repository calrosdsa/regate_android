package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.compoundmodels.UserProfileSala
import app.regate.models.UserSala
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserSalaDao:RoomEntityDao<UserSala>,UserSalaDao {
    @Transaction
    @Query("""
        select p.id,p.nombre,p.apellido,p.profile_photo,ug.is_admin,ug.id as user_sala_id from user_sala as ug
        inner join profiles as p on p.id = ug.profile_id
        where ug.sala_id = :id
    """)
    abstract override fun observeUsersSala(id: Long): Flow<List<UserProfileSala>>
}