package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Establecimiento
import app.regate.models.FavoriteEstablecimiento
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomFavoriteEstablecimientoDao:FavoriteEstablecimientoDao,RoomEntityDao<FavoriteEstablecimiento> {
    @Transaction
    @Query("select e.* from favorite_establecimiento as fe inner join establecimientos as e on e.id = fe.establecimiento_id")
    abstract override fun observeFavoriteEstablecimiento():Flow<List<Establecimiento>>
    @Transaction
    @Query("select establecimiento_id from favorite_establecimiento")
    abstract override fun observeFavoriteEstablecimientosIds():Flow<List<Long>>
    @Query("delete from favorite_establecimiento where establecimiento_id = :id")
    abstract override fun removeFavoriteEstablecimiento(id: Long)

    @Query("delete from favorite_establecimiento")
    abstract override fun removeAll()
}