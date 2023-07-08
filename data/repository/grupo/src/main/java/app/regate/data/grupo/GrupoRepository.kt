package app.regate.data.grupo

import app.regate.compoundmodels.MessageProfile
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.mappers.DtoToGrupo
import app.regate.data.mappers.DtoToProfile
import app.regate.data.mappers.DtoToUserGrupo
import app.regate.data.mappers.MessageDtoToMessage
import app.regate.data.mappers.ReplyMessageDtoToMessage
import app.regate.inject.ApplicationScope
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
    private val replyMessageMapper:ReplyMessageDtoToMessage,
    private val dtoToGrupo: DtoToGrupo,
    private val profileMapper:DtoToProfile,
    private val dtoToUserGrupo: DtoToUserGrupo,
    private val dispatchers: AppCoroutineDispatchers
){
//    suspend fun createSala(d:SalaRequestDto):ResponseMessage{
//        return grupoDataSourceImpl.createSala(d)
//    }
//    fun observeProfileSala(ids:List<Long>):Flow<List<Profile>>{
//        return profileDao.observeProfileSalas(ids)
//    }
//    suspend fun joinSala(salaId:Long,precio:Int):ResponseMessage{
//        val user  = userDao.getUser(0)
//        val dataR = JoinSalaRequest(
//            sala_id = salaId,
//            precio = precio,
//            profile_id = user.profile_id
//        )
//        return grupoDataSourceImpl.joinSala(dataR)
//    }
    suspend fun getReplyMessage(id:Long):MessageProfile{
        return messageProfileDao.getReplyMessage(id)
    }

    suspend fun getGrupo(id:Long):List<SalaDto>{
        return  grupoDataSourceImpl.getGrupo(id).also { result->
            val profiles = result.profiles.map { profileMapper.map(it) }
            val usersGrupo = result.profiles.map { dtoToUserGrupo.map(it,result.grupo.id) }
            profileDao.upsertAll(profiles)
            userGrupoDao.upsertAll(usersGrupo)
        }.salas
    }
    suspend fun filterGrupos(d:FilterGrupoData):List<GrupoDto>{
        return grupoDataSourceImpl.filterGrupos(d).also {res->
            grupoDao.deleteAll()
            val grupos = res.map { dtoToGrupo.map(it) }
            grupoDao.upsertAll(grupos)
        }
    }
    suspend fun saveMessage(data:GrupoMessageDto){
        messageProfileDao.upsert(messageMapper.map(data))
    }

    suspend fun getUsersGroup(id:Long){
        withContext(dispatchers.computation){
        grupoDataSourceImpl.getUsersGrupo(id).apply {
            val profiles = map { profileMapper.map(it) }
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