package app.regate.data.grupo

import app.regate.compoundmodels.MessageProfile
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.chat.RequestChatUnreadMessages
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoPendingRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse
import app.regate.data.dto.empresa.grupo.PaginationPendingRequestUser
import app.regate.data.dto.empresa.grupo.PaginationUserGrupoRequest
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.PendingRequestCount
import app.regate.data.dto.empresa.grupo.setting.GrupoInvitationLinkDto
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.mappers.DtoToGrupo
import app.regate.data.mappers.DtoToUserGrupo
import app.regate.data.mappers.MessageDtoToMessage
import app.regate.data.mappers.MessageToMessageDto
import app.regate.data.mappers.ReplyMessageDtoToMessage
import app.regate.data.mappers.UserGroupDtoToProfile
import app.regate.inject.ApplicationScope
import app.regate.models.Message
import app.regate.models.MyGroups
import app.regate.models.Profile
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class GrupoRepository(
    private val grupoDataSourceImpl: GrupoDataSourceImpl,
    private val grupoDao: GrupoDao,
    private val userGrupoDao: UserGrupoDao,
    private val profileDao: ProfileDao,
    private val messageProfileDao: MessageProfileDao,
    private val messageMapper:MessageDtoToMessage,
    private val messageMapperDto:MessageToMessageDto,
    private val replyMessageMapper:ReplyMessageDtoToMessage,
    private val dtoToGrupo: DtoToGrupo,
    private val dtoToUserGrupo: DtoToUserGrupo,
    private val dispatchers: AppCoroutineDispatchers,
    private val userDao: UserDao,
    private val myGroupsDao: MyGroupsDao,
    private val profileMapper:UserGroupDtoToProfile
){


    suspend fun searchGrupos(d:SearchFilterRequest,page:Int =1,size:Int=5):PaginationGroupsResponse{
        return grupoDataSourceImpl.searchGrupos(d,page, size)
    }
    fun observeMessages(id: Long): Flow<List<MessageProfile>> {
        return messageProfileDao.getMessages(id)
    }
    suspend fun getGroupsWhereUserIsAdmin():List<GrupoDto>{
        return grupoDataSourceImpl.getGroupsWhereUserIsAdmin()
    }
    suspend fun sendShareMessage(d:List<GrupoMessageDto>){
            grupoDataSourceImpl.sendShareMessage(d)
    }
    suspend fun myGroups(){
        withContext(dispatchers.computation){
            val response = grupoDataSourceImpl.myGroups()
            val grupos = response.map { dtoToGrupo.map(it) }
            val myGroups = response.map { MyGroups(
                id = it.id,
                request_estado = GrupoRequestEstado.fromInt(it.grupo_request_estado),
                )}
            myGroupsDao.deleteMyGroups(GrupoRequestEstado.JOINED.ordinal)
            myGroupsDao.upsertAll(myGroups)
            grupoDao.upsertAll(grupos)
        }
    }

    suspend fun myGroupsRequest(){
        withContext(dispatchers.computation){
            val response = grupoDataSourceImpl.myGroupsRequest()
//            val grupos = response.map { dtoToGrupo.map(it) }
            val myGroups = response.map { MyGroups(
                id = it.id,
                request_estado = GrupoRequestEstado.fromInt(it.grupo_request_estado
                ))}
            myGroupsDao.deleteMyGroups(GrupoRequestEstado.PENDING.ordinal)
            myGroupsDao.upsertAll(myGroups)
//            grupoDao.upsertAll(grupos)
        }
    }
    suspend fun removeUserFromGroup(id:Long) {
        withContext(dispatchers.computation) {
            try {
                grupoDataSourceImpl.removeUserFromGroup(id)
                userGrupoDao.deleteUserGroup(id)
            } catch (e: Exception) {
                //TODO()
            }
        }
    }
    suspend fun deleteGroupUserLocal(groupId:Long){
        myGroupsDao.deleteByGroupId(groupId)
    }

    suspend fun changeStatusUser(id:Long,status:Boolean) {
        withContext(dispatchers.computation) {
            try {
                grupoDataSourceImpl.changeStatusUser(id, status)
                userGrupoDao.updateUser(id,status)
            } catch (e: Exception) {
                //TODO
            }
        }
    }
    suspend fun syncMessages(grupoId: Long){
        withContext(dispatchers.io){
            try {
                val user = userDao.getUser(0)
                val messages = messageProfileDao.getUnSendedMessage(user.profile_id, grupoId)
                val data = messages.map { messageMapperDto.map(it) }
                if (messages.isEmpty()) return@withContext
                val results = grupoDataSourceImpl.syncMessages(data).map {
                    messageMapper.map(it).copy(readed = true)
                }
                messageProfileDao.upsertAll(results)
            } catch (e:Exception){
                //TODO()
            }

        }
    }
    suspend fun createGrupo(d:GroupRequest):GrupoDto{
        return grupoDataSourceImpl.createGroup(d).also {
            grupoDao.upsert(dtoToGrupo.map(it))
            myGroupsDao.upsert(MyGroups(id = it.id))
        }
    }
    suspend fun joinGrupo(grupoId:Long,visibility:Int=2,){
        try{
        val user  = userDao.getUser(0)
        val requestEstado= if(visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
        if(visibility == GrupoVisibility.PUBLIC.ordinal){
        val dataR = AddUserGrupoRequest(
            grupo_id = grupoId,
            profile_id = user.profile_id
        )
        grupoDataSourceImpl.joinGrupo(dataR).also {
            myGroupsDao.upsert(
                MyGroups(
                    id = grupoId,
                    request_estado = GrupoRequestEstado.fromInt(requestEstado)
            )
            )
        }
        }else{
            val d = PendingRequest(
                grupo_id = grupoId,
                profile_id = user.profile_id
            )
            addPendingRequest(d)
//            grupoDataSourceImpl.addPendingRequest(d)
        }
        }catch (e:Exception){
            //TODO()
        }
    }
    suspend fun getGrupoDetail(id:Long):List<SalaDto>{
        return  grupoDataSourceImpl.getGrupoDetail(id).also { result->
            userGrupoDao.deleteUsersGroup(id)
            grupoDao.upsert(dtoToGrupo.map(result.grupo))
            val profiles = result.profiles.map { profileMapper.map(it) }
            val usersGrupo = result.profiles.map { dtoToUserGrupo.map(it,result.grupo.id) }
            profileDao.upsertAll(profiles)
            userGrupoDao.upsertAll(usersGrupo)
        }.salas
    }
    suspend fun getGrupo(id:Long):GrupoDto{
        return grupoDataSourceImpl.getGrupo(id)
    }
    suspend fun filterGrupos(d:FilterGrupoData,page: Int):PaginationGroupsResponse{
        return grupoDataSourceImpl.filterGrupos(d,page)
    }

    suspend fun getUsersGroup(id:Long){
        withContext(dispatchers.computation){ 
        grupoDataSourceImpl.getUsersGrupo(id).apply {
            userGrupoDao.deleteUsersGroup(id)
            val profiles = map {
//                profileMapper.map(it)
                Profile(
                    id = it.profile_id,
                    profile_photo = it.profile_photo,
                    nombre = it.nombre,
                    apellido = it.apellido,
                )
            }
            val usersGrupo = map { dtoToUserGrupo.map(it,id) }
            profileDao.upsertAll(profiles)
            userGrupoDao.upsertAll(usersGrupo)
        }
        }
    }
    suspend fun getMessagesGrupo(id:Long,page:Int):Int{
          return   try{
              val data = grupoDataSourceImpl.getMessagesGrupo(id,page)
              withContext(dispatchers.computation){
               data.let { apiResult ->
                val messages =async{ apiResult.results.map { messageMapper.map(it) } }
                val replies =async{ apiResult.results.filter { it.reply_to != null }.map {
                    replyMessageMapper.map(it.reply_message) }
                }
             val results = messages.await() + replies.await()
             messageProfileDao.upsertAll(results)
              }
             }
              data.page
         }catch (e:Exception){
             0
         }
    }
    //REQUEST
    suspend fun getPendingRequests(groupId:Long,page:Int,estado:GrupoPendingRequestEstado = GrupoPendingRequestEstado.PENDING):PaginationPendingRequestUser{
        return grupoDataSourceImpl.getPendingRequests(groupId,page,estado.ordinal)
    }
    suspend fun getUserRequest(page:Int):PaginationUserGrupoRequest{
        return grupoDataSourceImpl.getUserRequest(page)
    }
    suspend fun declinePendingRequest(d: PendingRequest){
        grupoDataSourceImpl.declinePendingRequest(d).also {
            myGroupsDao.deleteByGroupId(d.grupo_id)
        }
    }
    suspend fun cancelPendingRequest(d: PendingRequest){
        grupoDataSourceImpl.cancelPendingRequest(d).also {
            myGroupsDao.deleteByGroupId(d.grupo_id)
        }
    }
    suspend fun addPendingRequest(d: PendingRequest){
        grupoDataSourceImpl.addPendingRequest(d).also {
            myGroupsDao.upsert(
                MyGroups(
                    id = d.grupo_id,
                    request_estado = GrupoRequestEstado.PENDING
                )
            )
        }
    }
    suspend fun confirmPendingRequest(d:PendingRequest){
        grupoDataSourceImpl.confirmPendingRequest(d)
    }
    suspend fun getPendingRequestCount(grupoId:Long):PendingRequestCount{
        return grupoDataSourceImpl.getPendingRequestCount(grupoId)
    }

    //SETTING
    suspend fun getGrupoByIdLink(idLink:String):GrupoDto{
        return grupoDataSourceImpl.getGrupoByIdLink(idLink)
    }
    suspend fun getOrInsertInvitationLink(id:Long):GrupoInvitationLinkDto{
        return grupoDataSourceImpl.getOrInsertInvitationLink(id)
    }
    suspend fun resetInvitationLink(id:Long):GrupoInvitationLinkDto{
        return grupoDataSourceImpl.resetInvitationLink(id)
    }
}