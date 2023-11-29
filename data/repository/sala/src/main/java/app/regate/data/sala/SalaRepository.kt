package app.regate.data.sala

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.daos.ChatDao
import app.regate.data.daos.InstalacionDao
import app.regate.data.daos.MessageSalaDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.chat.IdDto
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
import app.regate.data.mappers.users.ProfileToProfileBaseDto
import app.regate.inject.ApplicationScope
import app.regate.models.chat.MessageSala
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
    private val userDao: UserDao,
    private val instalacionDao: InstalacionDao,
    private val chatDao: ChatDao,
//    private val userRoomDao: UserRoomDao,
    private val dispatchers:AppCoroutineDispatchers,
    private val profileToProfileBaseDto: ProfileToProfileBaseDto
//    private val salaDtoToSalaEntity: SalaDtoToSalaEntity
){

    suspend fun searchSalas(d:SearchFilterRequest,page: Int,size:Int):PaginationSalaResponse{
        return salaDataSourceImpl.searchSalas(d,page,size)
    }

    suspend fun getSalaCompleteHistory(salaId: Long):SalaCompleteDetail{
        return salaDataSourceImpl.getCompleteSalaHistory(salaId)
    }
    suspend fun salaComplete(d:CompleteSalaRequest) {
        withContext(dispatchers.io) {
            try {
                salaDataSourceImpl.completeSala(d)
            } catch (e: Exception) {
                throw e
            }
        }
    }
    suspend fun deleteComplete(d:CompleteSalaRequest){
        withContext(dispatchers.io){
            try{
                salaDataSourceImpl.deleteComplete(d)
            }catch (e:Exception){
                throw  e
            }
        }
    }
    suspend fun createSala(d:SalaRequestDto){
        withContext(dispatchers.io){
         try{
              val res = salaDataSourceImpl.createSala(d)
             val instalacion = instalacionDao.getInstalacion(d.instalacion_id)
             if( instalacion != null){
                 chatDao.upsert(Chat(
                     parent_id = res.id,
                     type_chat = TypeChat.TYPE_CHAT_SALA.ordinal,
                     id = res.chat_id,
                     name = d.titulo,
                     photo = instalacion.portada
                 ))
             }
         }catch (e:Exception){
             throw e
         }
        }
    }
    fun observeProfileSala(ids:List<Long>):Flow<List<Profile>>{
        return profileDao.observeProfileSalas(ids)
    }
    suspend fun joinSala(salaId:Long,precio:Double,cupos:Int,grupoId:Long):IdDto{
        val user  = userDao.getUser(0)
        val dataR = JoinSalaRequest(
            sala_id = salaId,
            precio_sala = precio,
            profile_id = user.profile_id,
            cupos = cupos,
            grupo_Id = grupoId,
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



    suspend fun getSalasUser(page:Int):PaginationSalaResponse{
      return salaDataSourceImpl.getSalasUser(page)
    }

}