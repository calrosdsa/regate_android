package app.regate.data.sala

import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.mappers.DtoToProfile
import app.regate.data.mappers.MessageDtoToMessage
import app.regate.inject.ApplicationScope
import app.regate.models.Profile
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SalaRepository(
    private val salaDataSourceImpl: SalaDataSourceImpl,
    private val profileDao: ProfileDao,
    private val messageProfileDao: MessageProfileDao,
    private val messageMapper:MessageDtoToMessage,
    private val profileMapper:DtoToProfile,
    private val userDao: UserDao,
){
    suspend fun createSala(d:SalaRequestDto):ResponseMessage{
        return salaDataSourceImpl.createSala(d)
    }
    fun observeProfileSala(ids:List<Long>):Flow<List<Profile>>{
        return profileDao.observeProfileSalas(ids)
    }
    suspend fun joinSala(salaId:Long,precio:Int):ResponseMessage{
        val user  = userDao.getUser(0)
        val dataR = JoinSalaRequest(
            sala_id = salaId,
            precio = precio,
            profile_id = user.profile_id
        )
        return salaDataSourceImpl.joinSala(dataR)
    }
    suspend fun getSala(id:Long):SalaDetail{
        return  salaDataSourceImpl.getSala(id).also { result->
            val profiles = result.profiles.map { profileMapper.map(it) }
            profileDao.upsertAll(profiles)
        }
    }

    suspend fun getSalas(id:Long):List<SalaDto>{
        return  salaDataSourceImpl.getSalas(id)
    }

    suspend fun filterSalas(d:SalaFilterData,page:Int = 1):PaginationSalaResponse{
        return salaDataSourceImpl.filterSalas(d,page)
    }
    suspend fun getGrupoSalas(id:Long,page:Int = 1):PaginationSalaResponse{
        return salaDataSourceImpl.getGrupoSalas(id,page)
    }

    suspend fun saveMessage(data: GrupoMessageDto){
        messageProfileDao.upsert(messageMapper.map(data))
    }

    suspend fun getMessagesSala(id:Long):List<GrupoMessageDto>{
        return salaDataSourceImpl.getMessagesSala(id).also { apiResult ->
                val messages = apiResult.map { messageMapper.map(it)}
                messageProfileDao.upsertAll(messages)
        }
    }
}