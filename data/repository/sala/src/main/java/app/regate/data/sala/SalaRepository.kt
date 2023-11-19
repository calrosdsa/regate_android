package app.regate.data.sala

import app.regate.data.daos.ChatDao
import app.regate.data.daos.InstalacionDao
import app.regate.data.daos.MessageSalaDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.CompleteSalaRequest
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.mappers.MessageDtoToMessageSala
import app.regate.data.mappers.MessageToMessageSalaDto
import app.regate.inject.ApplicationScope
import app.regate.models.MessageSala
import app.regate.models.user.Profile
import app.regate.models.chat.Chat
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.toInstant
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SalaRepository(
    private val salaDataSourceImpl: SalaDataSourceImpl,
    private val profileDao: ProfileDao,
//    private val messageProfileDao: MessageProfileDao,
    private val messageSalaDao: MessageSalaDao,
    private val messageMapperSalaDto:MessageToMessageSalaDto,
    private val messageMapperSala:MessageDtoToMessageSala,
    private val userDao: UserDao,
    private val instalacionDao: InstalacionDao,
    private val chatDao: ChatDao,
//    private val userRoomDao: UserRoomDao,
    private val dispatchers:AppCoroutineDispatchers,
//    private val salaDtoToSalaEntity: SalaDtoToSalaEntity
){

    suspend fun searchSalas(d:SearchFilterRequest,page: Int,size:Int):PaginationSalaResponse{
        return salaDataSourceImpl.searchSalas(d,page,size)
    }

    suspend fun getSalaCompleteHistory(salaId: Long):SalaCompleteDetail{
        return salaDataSourceImpl.getCompleteSalaHistory(salaId)
    }
    suspend fun salaComplete(d:CompleteSalaRequest){
        salaDataSourceImpl.completeSala(d)
    }
    suspend fun createSala(d:SalaRequestDto){
        withContext(dispatchers.io){
         try{
              val res = salaDataSourceImpl.createSala(d)
             val instalacion = instalacionDao.getInstalacion(d.instalacion_id)
             if( instalacion != null){
             val chat = Chat(
                 parent_id = res.id,
                 type_chat = TypeChat.TYPE_CHAT_SALA.ordinal,
                 id = res.chat_id,
                 name = instalacion.name,
                 photo = instalacion.portada
             )
                 chatDao.upsert(chat)
             }
         }catch (e:Exception){
             throw e
         }
        }
    }
    fun observeProfileSala(ids:List<Long>):Flow<List<Profile>>{
        return profileDao.observeProfileSalas(ids)
    }
    suspend fun joinSala(salaId:Long,precio:Double,cupos:Int,grupoId:Long):ResponseMessage{
        val user  = userDao.getUser(0)
        val dataR = JoinSalaRequest(
            sala_id = salaId,
            precio_sala = precio,
            profile_id = user.profile_id,
            cupos = cupos,
            grupo_Id = grupoId
        )
        return salaDataSourceImpl.joinSala(dataR)
    }
    suspend fun exitSala(id:Int){
        salaDataSourceImpl.exitSala(id)
    }
    suspend fun getSala(id:Long):SalaDetail{
        return  salaDataSourceImpl.getSala(id)
    }

    suspend fun getEstablecimientoSalas(id:Long):List<SalaDto>{
        return  salaDataSourceImpl.getEstablecimientoSalas(id)
    }

    suspend fun filterSalas(d:SalaFilterData,page:Int = 1):PaginationSalaResponse{
        return salaDataSourceImpl.filterSalas(d,page)
    }
    suspend fun getGrupoSalas(id:Long,page:Int = 1):PaginationSalaResponse{
        return salaDataSourceImpl.getGrupoSalas(id,page)
    }

    suspend fun saveMessage(data:MessageSalaDto){
        messageSalaDao.upsert(messageMapperSala.map(data))
    }
    suspend fun saveMessageLocal(data: MessageSala){
        messageSalaDao.upsert(data)
    }
    suspend fun syncMessages(salaId: Long){
        withContext(dispatchers.io){
            try {
                val user = userDao.getUser(0)
                val messages = messageSalaDao.getUnSendedMessage(user.profile_id, salaId )
                val data = messages.map { messageMapperSalaDto.map(it) }
                if (messages.isEmpty()) return@withContext
                val results = salaDataSourceImpl.syncMessages(data).map {
                    messageMapperSala.map(it)
                }
                messageSalaDao.upsertAll(results)
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    suspend fun getSalasUser(page:Int):PaginationSalaResponse{
      return salaDataSourceImpl.getSalasUser(page)
    }
     suspend fun getMessagesSala(id:Long,page:Int):Int{
        return   try{
            val data = salaDataSourceImpl.getMessagesSala(id,page)
            withContext(dispatchers.computation) {
                data.let { apiResult ->
                    val messages = async {
                        apiResult.results.map {
                            messageMapperSala.map(it)
                        }
                    }
                    val replies = async {
                        apiResult.results.filter { it.reply_to != null }.map {
                            MessageSala(
                                id = it.reply_message.id,
                                sala_id = it.reply_message.sala_id,
                                profile_id = it.reply_message.profile_id,
                                content = it.reply_message.content,
                                reply_to = it.reply_message.reply_to,
                                created_at = it.reply_message.created_at.toInstant()
                            )
                        }
                    }
                val results = messages.await() + replies.await()
                messageSalaDao.upsertAll(results)
                }
            }
            data.nextPage
            }catch (e:Exception){
            0
        }

    }
}