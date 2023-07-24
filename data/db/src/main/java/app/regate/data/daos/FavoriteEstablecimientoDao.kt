package app.regate.data.daos

import app.regate.models.Establecimiento
import app.regate.models.FavoriteEstablecimiento
import kotlinx.coroutines.flow.Flow

interface FavoriteEstablecimientoDao:EntityDao<FavoriteEstablecimiento> {
    fun observeFavoriteEstablecimiento():Flow<List<Establecimiento>>
    fun removeAll()
    fun removeFavoriteEstablecimiento(id:Long)
    fun observeFavoriteEstablecimientosIds():Flow<List<Long>>
}