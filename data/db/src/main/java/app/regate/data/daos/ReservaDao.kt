package app.regate.data.daos

import app.regate.compoundmodels.ReservaDetail
import app.regate.models.Instalacion
import app.regate.models.Reserva
import kotlinx.coroutines.flow.Flow

interface ReservaDao:EntityDao<Reserva> {
    fun observeReservas():Flow<List<Reserva>>
    fun observeReservaDetail(id:Long):Flow<ReservaDetail>
    suspend fun deleteAll()
    suspend fun deleteByIds(ids:List<Long>)
    suspend fun updateReserva(id:Long,estado:Int)
    suspend fun updateDescriptionReserva(descrption:String,id:Long)
}