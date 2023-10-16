package app.regate.data.reserva

import app.regate.data.daos.ReservaDao
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest
import app.regate.data.mappers.CupoToCupoDto
import app.regate.inject.ApplicationScope
import app.regate.models.Cupo
import app.regate.models.Reserva
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class ReservaRepository(
    private val reservaDataSourceImpl: ReservaDataSourceImpl,
    private val cupoToReservaRequest: CupoToCupoDto,
    private val dispatchers: AppCoroutineDispatchers,
    private val reservaDao: ReservaDao
//    private val cupoDao: CupoDao
) {
    suspend fun getReserva(id: Long): ReservaDto {
        return reservaDataSourceImpl.getReserva(id)
    }

    suspend fun updateReservas() {
        withContext(dispatchers.computation){
            try{
                val res = reservaDataSourceImpl.getReservas().let {results->
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
                            created_at = it.created_at
                        )
                    }
                }
                reservaDao.deleteAll()
                reservaDao.upsertAll(res)
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