package app.regate.data.reserva

import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDetail
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest

interface ReservaDataSource {
//    suspend fun me(): Me
//    suspend fun login(d:LoginRequest):LoginResponse
    suspend fun getReserva(id:Long):ReservaDetail
    suspend fun getReservas():List<ReservaDto>
    suspend fun confirmarReservas(data:ReservaRequest): ResponseMessage
}

