package app.regate.data.grupo

import GrupoInvitationRequest
import PaginationInvitationResponse
import PaginationUserInvitationsResponse
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.grupo.JoinUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.grupo.JoinGrupoResponse
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse
import app.regate.data.dto.empresa.grupo.PaginationPendingRequestUser
import app.regate.data.dto.empresa.grupo.PaginationUserGrupoRequest
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.PendingRequestCount
import app.regate.data.dto.empresa.grupo.setting.GrupoInvitationLinkDto

interface GrupoDataSource {
   suspend fun myGroups():List<GrupoDto>
   suspend fun myGroupsRequest():List<GrupoDto>
   suspend fun filterGrupos(d:FilterGrupoData,page:Int):PaginationGroupsResponse
   suspend fun getGrupoDetail(id:Long):GrupoResponse
   suspend fun getGrupo(id:Long):GrupoDto
   suspend fun joinGrupo(d:JoinUserGrupoRequest): JoinGrupoResponse
   suspend fun createGroup(d:GroupRequest):GrupoDto
   suspend fun removeUserFromGroup(id:Long)
   suspend fun changeStatusUser(id:Long,status:Boolean)
   suspend fun getGroupsWhereUserIsAdmin():List<GrupoDto>
   suspend fun searchGrupos(d: SearchFilterRequest, page:Int, size: Int): PaginationGroupsResponse


   //REQUESTS
   suspend fun getUserRequest(page:Int): PaginationUserGrupoRequest
   suspend fun getPendingRequests(groupId:Long, page:Int,estado:Int): PaginationPendingRequestUser
   suspend fun declinePendingRequest(d:PendingRequest)
   suspend fun cancelPendingRequest(d:PendingRequest)
   suspend fun addPendingRequest(d:PendingRequest)
   suspend fun confirmPendingRequest(d:PendingRequest)
   suspend fun getPendingRequestCount(groupId: Long):PendingRequestCount

   //Invitation
   suspend fun sendInvitation(d:GrupoInvitationRequest)
   suspend fun acceptInvitation(d:GrupoInvitationRequest)
   suspend fun declineInvitation(d:GrupoInvitationRequest)

   suspend fun getInvitationUsers(groupId: Long,page: Int):PaginationInvitationResponse
   suspend fun getUserInvitations(page: Int):PaginationUserInvitationsResponse



   //Setting
   suspend fun getGrupoByIdLink(idLink:String):GrupoDto
   suspend fun getOrInsertInvitationLink(id:Long):GrupoInvitationLinkDto
   suspend fun resetInvitationLink(id:Long):GrupoInvitationLinkDto

//   suspend fun userGroups()
//   suspend fun createGrupo(d: SalaRequestDto):ResponseMessage

}

