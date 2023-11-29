package app.regate.data.grupo

import GrupoInvitationRequest
import InvitationEstado
import app.regate.data.daos.ChatDao
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.JoinUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoPendingRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import app.regate.data.dto.empresa.grupo.JoinGroupResponse
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse
import app.regate.data.dto.empresa.grupo.PaginationPendingRequestUser
import app.regate.data.dto.empresa.grupo.PaginationUserGrupoRequest
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.PendingRequestCount
import app.regate.data.dto.empresa.grupo.setting.GrupoInvitationLinkDto
import app.regate.data.mappers.DtoToGrupo
import app.regate.data.mappers.users.ProfileToProfileBaseDto
import app.regate.inject.ApplicationScope
import app.regate.models.grupo.MyGroups
import app.regate.models.user.Profile
import app.regate.models.chat.Chat
import app.regate.models.grupo.Grupo
import app.regate.models.grupo.InvitationGrupo
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class GrupoRepository(
    private val grupoDataSourceImpl: GrupoDataSourceImpl,
    private val grupoDao: GrupoDao,
    private val userGrupoDao: UserGrupoDao,
    private val profileDao: ProfileDao,
    private val chatDao: ChatDao,
    private val dtoToGrupo: DtoToGrupo,
    private val dispatchers: AppCoroutineDispatchers,
    private val userDao: UserDao,
    private val myGroupsDao: MyGroupsDao,
    private val profileToProfileBaseDto: ProfileToProfileBaseDto
){


    suspend fun searchGrupos(d:SearchFilterRequest,page:Int =1,size:Int=5):PaginationGroupsResponse{
        return grupoDataSourceImpl.searchGrupos(d,page, size)
    }
    suspend fun getGroupsWhereUserIsAdmin():List<GrupoDto>{
        return grupoDataSourceImpl.getGroupsWhereUserIsAdmin()
    }
    suspend fun myGroups(){
        withContext(dispatchers.computation){
            val response = grupoDataSourceImpl.myGroups()
            val grupos = response.map { dtoToGrupo.map(it) }
            val myGroups = response.map { MyGroups(
                id = it.id,
                request_estado = GrupoRequestEstado.fromInt(it.grupo_request_estado),
                )
            }
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
                ))
            }
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
    suspend fun leaveGrupo(id:Long,chatId: Long,grupoId: Long) {
        withContext(dispatchers.computation) {
            try {
                grupoDataSourceImpl.removeUserFromGroup(id)
                userGrupoDao.updateUserIsOut(id,true)
                myGroupsDao.deleteByGroupId(grupoId)
                chatDao.updateWhenUserLeave(chatId)
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
    suspend fun createGrupo(d:GroupRequest):GrupoDto{
        return grupoDataSourceImpl.createGroup(d).also {grupo->
            grupoDao.upsert(dtoToGrupo.map(grupo))
            myGroupsDao.upsert(MyGroups(id = grupo.id))
            val chat = chatDao.getChatByType(grupo.id,TypeChat.TYPE_CHAT_GRUPO.ordinal)
            if(chat == null && grupo.chat_id != 0L){
            val c = Chat(
                parent_id = grupo.id,
                id = grupo.chat_id,
                type_chat = TypeChat.TYPE_CHAT_GRUPO.ordinal,
                name = grupo.name,
                photo = grupo.photo
            )
            chatDao.upsert(c)
            }
        }
    }
    suspend fun joinGrupo(grupoId:Long,visibility:Int=2,grupoDto: GrupoDto?):JoinGroupResponse? {
        try {
            val user = userDao.getUser(0)
            val profile = profileDao.getProfile(user.profile_id)
            val requestEstado = if (visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
            if (visibility == GrupoVisibility.PUBLIC.ordinal) {
                val userGroup = userGrupoDao.getUserGroup(grupoId,user.profile_id)
                val dataR = JoinUserGrupoRequest(
                    grupo_id = grupoId,
                    profile_id = user.profile_id,
                    profile = profileToProfileBaseDto.map(profile),
                    id = userGroup?.id?:0
                )
                return grupoDataSourceImpl.joinGrupo(dataR).also { response ->
                    if (grupoDto != null) {
                        chatDao.upsert(
                            Chat(
                                id = response.chat_id,
                                parent_id = grupoDto.id,
                                name = grupoDto.name,
                                photo = grupoDto.photo,
                                type_chat = TypeChat.TYPE_CHAT_GRUPO.ordinal

                            )
                        )
                    }
                    myGroupsDao.upsert(
                        MyGroups(
                            id = grupoId,
                            request_estado = GrupoRequestEstado.fromInt(requestEstado)
                        )
                    )
                }
            } else {
                val d = PendingRequest(
                    grupo_id = grupoId,
                    profile_id = user.profile_id
                )
                addPendingRequest(d)
                return null
//            grupoDataSourceImpl.addPendingRequest(d)
            }
        } catch (e: Exception) {
            //TODO()
            return null
        }
    }
    suspend fun getGrupoDetail(id:Long):GrupoResponse{
        return  grupoDataSourceImpl.getGrupoDetail(id).also { result->
//            userGrupoDao.deleteUsersGroup(id)
            grupoDao.upsert(dtoToGrupo.map(result.grupo))
//            val profiles = result.profiles.map { profileMapper.map(it) }
//            val usersGrupo = result.profiles.map { dtoToUserGrupo.map(it,result.grupo.id) }
//            profileDao.upsertAll(profiles)
//            userGrupoDao.upsertAll(usersGrupo)
        }
    }
    suspend fun getGrupo(id:Long):GrupoDto{
        return grupoDataSourceImpl.getGrupo(id)
    }
    suspend fun filterGrupos(d:FilterGrupoData,page: Int):PaginationGroupsResponse{
        return grupoDataSourceImpl.filterGrupos(d,page)
    }
    suspend fun updateGruposSource(page: Int):Int{
        return try{
            val res = grupoDataSourceImpl.filterGrupos(FilterGrupoData(category_id = 1),page)
            withContext(dispatchers.io){
                try{
                    val grupos = res.results.map{ dtoToGrupo.map(it)}
                    grupoDao.insertAllonConflictIgnore(grupos)
                }catch(e:Exception){
                    throw e
                }
            }
            res.page
        }catch(e:Exception){
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
    //Invitation
    suspend fun updateInvitationSource(grupoId: Long,page: Int):Int{
        return try {
            val res = grupoDataSourceImpl.getInvitationUsers(groupId = grupoId, page = page)
            withContext(dispatchers.io) {
                try {
                    grupoDao.deleteInvitations(grupoId)
                    res.results.map {result->
                    grupoDao.insertInvitation(
                        InvitationGrupo(
                            profile_id = result.profile_id,
                            grupo_id = result.grupo_id,
                            estado = result.estado,
                        )
                    )
                    }
                    val profiles = res.results.map {result->
                            Profile(
                                id = result.profile_id,
                                nombre = result.nombre,
                                apellido = result.apellido,
                                profile_photo = result.profile_photo
                            )
                    }
                    profileDao.insertAllonConflictIgnore(profiles)
                } catch (e: java.lang.Exception) {
                    throw e
                }
            }
            res.page
        }catch (e:Exception){
            0
        }
    }
    suspend fun updateUserInvitationsSource(profileId:Long,page: Int):Int {
        return try {
            val res = grupoDataSourceImpl.getUserInvitations(page = page)
            withContext(dispatchers.io) {
                try {
                    grupoDao.deleteUserInvitations(profileId)
                    val grupos = res.results.map { result ->
                        Grupo(
                            id = result.grupo_id,
                            name = result.name,
                            photo = result.photo,
                        )
                    }
                    grupoDao.upsertAll(grupos)
                    res.results.map { result ->
                        grupoDao.insertInvitation(
                            InvitationGrupo(
                                profile_id = result.profile_id,
                                grupo_id = result.grupo_id,
                                estado = result.estado,
                            )
                        )
                    }
                } catch (e: Exception) {
                    throw e
                }
            }
            res.page
        } catch (e: Exception) {
            0
        }
    }
    suspend fun sendInvitation(d:GrupoInvitationRequest) {
        withContext(dispatchers.io) {
            try {
                grupoDataSourceImpl.sendInvitation(d)
                grupoDao.updateInvitationEstado(d.grupo_id,d.profile_id,InvitationEstado.PENDIENTE.ordinal)
            } catch (e: Exception) {
                throw e
            }
        }
    }
    suspend fun declineGrupoInvitation(d:GrupoInvitationRequest){
        withContext(dispatchers.io){
            try {
                grupoDataSourceImpl.declineInvitation(d)
                grupoDao.updateInvitationEstado(d.grupo_id,d.profile_id,InvitationEstado.NONE.ordinal)
            }catch (e:Exception){
                throw e
            }
        }
    }
    suspend fun acceptGrupoInvition(d:GrupoInvitationRequest){
        withContext(dispatchers.io){
            try {
                grupoDataSourceImpl.acceptInvitation(d)
                grupoDao.updateInvitationEstado(d.grupo_id,d.profile_id,InvitationEstado.ACEPTADO.ordinal)
            }catch (e:Exception){
                throw e
            }
        }
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