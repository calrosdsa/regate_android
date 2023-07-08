package app.regate.data.reserva

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest
import app.regate.data.mappers.CupoToCupoDto
import app.regate.inject.ApplicationScope
import app.regate.models.Cupo
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class ReservaRepository(
    private val reservaDataSourceImpl: ReservaDataSourceImpl,
    private val cupoToReservaRequest: CupoToCupoDto,
//    private val cupoDao: CupoDao
) {
    suspend fun getReserva(id: Long): ReservaDto {
        return reservaDataSourceImpl.getReserva(id)
    }

    suspend fun getReservas(): List<ReservaDto> {
        return reservaDataSourceImpl.getReservas()
    }

    suspend fun confirmarReservas(data: List<Cupo>, totalPrice: Int,paid:Int,endTime:String,establecimientoId:Long): ResponseMessage {
        val cupos = data.map { cupoToReservaRequest.map(it) }
        val requestData = ReservaRequest(cupos = cupos, total_price = totalPrice,
            paid = paid, end_time = endTime,establecimiento_id = establecimientoId)
        return reservaDataSourceImpl.confirmarReservas(requestData)
    }
}