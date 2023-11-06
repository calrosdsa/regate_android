package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import app.regate.compoundmodels.grupo.UserInvitation
import app.regate.compoundmodels.grupo.UserInvitationGrupo
import app.regate.models.grupo.Grupo
import app.regate.models.grupo.InvitationGrupo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomGrupoDao:GrupoDao,RoomEntityDao<Grupo>{
    @Transaction
    @Query("select * from grupos where id = :id")
    abstract override fun observeGrupo(id: Long): Flow<Grupo>
    @Transaction
    @Query("select * from grupos")
    abstract override fun observeGrupos(): Flow<List<Grupo>>

    @Transaction
    @Query("select * from invitation_grupo where grupo_id = :grupoId")
    abstract override fun observeInvitations(grupoId: Long): PagingSource<Int, UserInvitationGrupo>
    @Transaction
    @Query("select * from invitation_grupo where profile_id = :profileId")
    abstract override fun observeUserInvitations(profileId: Long): PagingSource<Int, UserInvitation>
    @Transaction
    @Query("select  *  from grupos")
    abstract override fun observeUserGroups(): Flow<List<Grupo>>

    @Query("update invitation_grupo set estado = :estado where grupo_id = :grupoId and profile_id = :profileId")
    abstract override suspend fun updateInvitationEstado(
        grupoId: Long,
        profileId: Long,
        estado: Int
    )
    @Upsert
    abstract override suspend fun insertInvitation(invitation: InvitationGrupo)

    @Query("delete from invitation_grupo where grupo_id = :grupoId")
    abstract override suspend fun deleteInvitations(grupoId: Long)

    @Query("delete from invitation_grupo where profile_id = :profileId")
    abstract override suspend fun deleteUserInvitations(profileId: Long)

    @Query("delete from grupos")
    abstract override fun deleteAll()

    @Query("select * from grupos where id = :id")
    abstract override fun getGrupo(id: Long): Grupo
}