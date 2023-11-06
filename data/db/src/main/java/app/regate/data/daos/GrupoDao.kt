package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.grupo.UserInvitation
import app.regate.compoundmodels.grupo.UserInvitationGrupo
import app.regate.models.grupo.Grupo
import app.regate.models.grupo.InvitationGrupo
import kotlinx.coroutines.flow.Flow

interface GrupoDao:EntityDao<Grupo> {
    fun observeGrupo(id:Long):Flow<Grupo>
    fun observeGrupos():Flow<List<Grupo>>
    fun observeUserGroups():Flow<List<Grupo>>
    fun observeInvitations(grupoId:Long):PagingSource<Int,UserInvitationGrupo>
    fun observeUserInvitations(profileId:Long):PagingSource<Int,UserInvitation>

    fun deleteAll()
    suspend fun updateInvitationEstado(grupoId:Long,profileId:Long,estado:Int)
    suspend fun insertInvitation(invitation:InvitationGrupo)
    suspend fun deleteInvitations(grupoId: Long)
    suspend fun deleteUserInvitations(profileId:Long)
    fun getGrupo(id: Long): Grupo
//    suspend fun updateGrupoLastMessage():
}