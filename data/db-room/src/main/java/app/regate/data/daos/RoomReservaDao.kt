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
    @Query("select * from reservas")
    abstract override fun observeReservas(): Flow<List<Reserva>>

    @Transaction
    @Query("select * from reservas where id = :id")
    abstract override fun observeReservaDetail(id: Long): Flow<ReservaDetail>
}