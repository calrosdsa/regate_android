package app.regate.data.grupo

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.grupo.PaginationGroupMessages
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse
import app.regate.data.dto.empresa.grupo.PaginationPendingRequestUser
import app.regate.data.dto.empresa.grupo.PaginationUserGrupoRequest
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.PendingRequestCount
import app.regate.data.dto.empresa.grupo.UserGrupoDto

interface GrupoDataSource {
   suspend fun myGroups():List<GrupoDto>
   suspend fun syncMessages(d:List<GrupoMessageDto>):List<GrupoMessageDto>
   suspend fun filterGrupos(d:FilterGrupoData,page:Int):PaginationGroupsResponse
   suspend fun getGrupoDetail(id:Long):GrupoResponse
   suspend fun getGrupo(id:Long):GrupoDto
   suspend fun getMessagesGrupo(id:Long,page:Int):PaginationGroupMessages
   suspend fun joinGrupo(d:AddUserGrupoRequest): ResponseMessage
   suspend fun getUsersGrupo(id:Long):List<UserGrupoDto>
   suspend fun createGroup(d:GroupRequest):GrupoDto
   suspend fun removeUserFromGroup(id:Long)
   suspend fun changeStatusUser(id:Long,status:Boolean)
   suspend fun getGroupsWhereUserIsAdmin():List<GrupoDto>
   suspend fun sendShareMessage(d:List<GrupoMessageDto>)
   suspend fun searchGrupos(d: SearchFilterRequest, page:Int, size: Int): PaginationGroupsResponse
   //REQUESTS
   suspend fun getUserRequest(page:Int): PaginationUserGrupoRequest
   suspend fun getPendingRequests(groupId:Long, page:Int,estado:Int): PaginationPendingRequestUser
   suspend fun declinePendingRequest(d:PendingRequest)
   suspend fun cancelPendingRequest(d:PendingRequest)
   suspend fun addPendingRequest(d:PendingRequest)
   suspend fun confirmPendingRequest(d:PendingRequest)
   suspend fun getPendingRequestCount(groupId: Long):PendingRequestCount


//   suspend fun userGroups()
//   suspend fun createGrupo(d: SalaRequestDto):ResponseMessage

}

