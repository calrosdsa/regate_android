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
        select p.id,p.nombre,p.apellido,p.profile_photo from user_grupo as ug
        inner join profiles as p on p.id = ug.profile_id
        where ug.grupo_id = :id
    """)
    abstract override fun observeUsersGrupo(id: Long): Flow<List<UserProfileGrupo>>
}