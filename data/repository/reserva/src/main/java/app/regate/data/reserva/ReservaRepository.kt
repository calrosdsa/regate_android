package app.regate.data.reserva

import app.regate.data.daos.LastUpdatedEntityDao
import app.regate.data.daos.ReservaDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest
import app.regate.data.mappers.CupoToCupoDto
import app.regate.inject.ApplicationScope
import app.regate.models.Cupo
import app.regate.models.LastUpdatedEntity
import app.regate.models.Reserva
import app.regate.models.UpdatedEntity
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class ReservaRepository(
    private val reservaDataSourceImpl: ReservaDataSourceImpl,
    private val cupoToReservaRequest: CupoToCupoDto,
    private val dispatchers: AppCoroutineDispatchers,
    private val reservaDao: ReservaDao,
    private val lastUpdateEntityDao: LastUpdatedEntityDao,
//    private val cupoDao: CupoDao
) {
    suspend fun updateReserva(id: Long) {
        withContext(dispatchers.io) {
            try {
                val res = reservaDataSourceImpl.getReserva(id)
                reservaDao.updateReserva(res.id,res.estado)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun updateDescriptionReserva(description:String,id: Long){
        withContext(dispatchers.io){
            reservaDao.updateDescriptionReserva(description,id)
        }
    }
    suspend fun deleteReservas(ids:List<Long>){
        withContext(dispatchers.io){
            try{
                reservaDao.deleteByIds(ids)
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    suspend fun updateReservas() {
        withContext(dispatchers.computation){
            try{
                val lastUpdatedEntity = lastUpdateEntityDao.getLastUpdatedEntity(UpdatedEntity.RESERVAS)?.created_at?: Clock.System.now()
                val res = reservaDataSourceImpl.getReservas(lastUpdatedEntity.toLocalDateTime(
                    TimeZone.currentSystemDefault()).toString()).let { results->
                    results.map {
                        Reserva(
                            id = it.id,
                            instalacion_id = it.instalacion_id,
                            instalacion_name = it.instalacion_name,
                            establecimiento_id = it.establecimiento_id,
                            user_id =it.profile_id,
                            pagado = it.paid,
                            total_price = it.total_price,
                            start_date = it.start_date,
                            end_date = it.end_date,
                            created_at = it.created_at,
                            instalacion_photo = it.instalacion_photo
                        )
                    }
                }
//                reservaDao.deleteAll()
                reservaDao.insertAllonConflictIgnore(res)
                lastUpdateEntityDao.upsert(LastUpdatedEntity(entity_id = UpdatedEntity.RESERVAS))
            }catch(e:Exception){
                throw e
            }
        }
    }

    suspend fun confirmarReservas(data: List<Cupo>, totalPrice: Int,paid:Int,endTime:String,establecimientoId:Long,instalacionId:Long): ResponseMessage {
        val cupos = data.map { cupoToReservaRequest.map(it) }
        val requestData = ReservaRequest(cupos = cupos, total_price = totalPrice,
            paid = paid, end_time = endTime,establecimiento_id = establecimientoId,instalacion_id = instalacionId)
        return reservaDataSourceImpl.confirmarReservas(requestData)
    }
}