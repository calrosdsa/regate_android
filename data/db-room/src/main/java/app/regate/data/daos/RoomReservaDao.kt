package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.ReservaDetail
import app.regate.models.Instalacion
import app.regate.models.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomReservaDao:RoomEntityDao<Reserva> ,ReservaDao {
    @Transaction
    @Query("select * from reservas order by start_date desc")
    abstract override fun observeReservas(): Flow<List<Reserva>>

    @Transaction
    @Query("select * from reservas where id = :id")
    abstract override fun observeReservaDetail(id: Long): Flow<ReservaDetail>

    @Transaction
    @Query("delete from reservas")
    abstract override suspend fun deleteAll()

    @Query("delete from reservas where id in (:ids)")
    abstract override suspend fun deleteByIds(ids: List<Long>)

    @Query("update reservas set description = :descrption where id = :id")
    abstract override suspend fun updateDescriptionReserva(descrption: String, id: Long)
    @Query("update reservas set estado = :estado where id = :id")
    abstract override suspend fun updateReserva(id: Long,estado:Int)
}