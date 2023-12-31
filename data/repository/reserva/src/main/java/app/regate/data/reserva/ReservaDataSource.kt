package app.regate.data.reserva

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest
import kotlinx.datetime.Instant

interface ReservaDataSource {
//    suspend fun me(): Me
//    suspend fun login(d:LoginRequest):LoginResponse
    suspend fun getReserva(id:Long):ReservaDto
    suspend fun getReservas(lastRequested:String):List<ReservaDto>
    suspend fun confirmarReservas(data:ReservaRequest): ResponseMessage
}

