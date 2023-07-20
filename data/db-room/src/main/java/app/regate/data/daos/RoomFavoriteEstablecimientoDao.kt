package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.FavoriteEstablecimiento

@Dao
abstract class RoomFavoriteEstablecimientoDao:FavoriteEstablecimientoDao,RoomEntityDao<FavoriteEstablecimiento> {
    @Transaction
    @Query("select * from favorite_establecimiento as fe inner join establecimientos as e on e.id = fe.establecimiento_id")
    abstract override fun observeFavoriteEstablecimiento()
}