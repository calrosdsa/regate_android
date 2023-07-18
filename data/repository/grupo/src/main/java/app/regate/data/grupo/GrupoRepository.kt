package app.regate.data.grupo

import app.regate.compoundmodels.MessageProfile
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.mappers.DtoToGrupo
import app.regate.data.mappers.DtoToProfile
import app.regate.data.mappers.DtoToUserGrupo
import app.regate.data.mappers.MessageDtoToMessage
import app.regate.data.mappers.MessageToMessageDto
import app.regate.data.mappers.ReplyMessageDtoToMessage
import app.regate.inject.ApplicationScope
import app.regate.models.Message
import app.regate.models.Profile
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
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
){
    suspend fun myGroups(){
        withContext(dispatchers.computation){
            val grupos = grupoDataSourceImpl.myGroups().map { dtoToGrupo.map(it) }
            grupoDao.upsertAll(grupos)
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
                    messageMapper.map(it)
                }
                messageProfileDao.upsertAll(results)
            }catch (e:Exception){
                //TODO()
            }

        }
    }
    suspend fun createGrupo(d:GroupRequest):GrupoDto{
        return grupoDataSourceImpl.createGroup(d).also {
            grupoDao.upsert(dtoToGrupo.map(it))
        }
    }
    suspend fun joinGrupo(grupoId:Long): ResponseMessage {
        val user  = userDao.getUser(0)
        val dataR = AddUserGrupoRequest(
            grupo_id = grupoId,
            profile_id = user.profile_id
        )
        return grupoDataSourceImpl.joinGrupo(dataR)
    }
    suspend fun getGrupo(id:Long):List<SalaDto>{
        return  grupoDataSourceImpl.getGrupo(id).also { result->
//            val profiles = result.profiles.map { profileMapper.map(it) }
            val usersGrupo = result.profiles.map { dtoToUserGrupo.map(it,result.grupo.id) }
//            profileDao.upsertAll(profiles)
            userGrupoDao.upsertAll(usersGrupo)
        }.salas
    }
    suspend fun filterGrupos(d:FilterGrupoData,page: Int):List<GrupoDto>{
        return grupoDataSourceImpl.filterGrupos(d,page)
    }
    suspend fun saveMessage(data:GrupoMessageDto){
        messageProfileDao.upsert(messageMapper.map(data))
    }

    suspend fun saveMessageLocal(data:Message){
        messageProfileDao.upsert(data)
    }
    suspend fun getUsersGroup(id:Long){
        withContext(dispatchers.computation){
            userGrupoDao.deleteUsers(id)
        grupoDataSourceImpl.getUsersGrupo(id).apply {
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
    suspend fun getMessagesGrupo(id:Long,page:Int){
         withContext(dispatchers.computation){
             try{
         grupoDataSourceImpl.getMessagesGrupo(id,page).also { apiResult ->
                val messages =async{ apiResult.map { messageMapper.map(it) } }
                val replies =async{ apiResult.filter { it.reply_to != null }.map {
                    replyMessageMapper.map(it.reply_message) }
                }
             val results = messages.await() + replies.await()
             messageProfileDao.upsertAll(results)
             }
         }catch (e:Exception){
             //TODO()
         }
        }
    }
}