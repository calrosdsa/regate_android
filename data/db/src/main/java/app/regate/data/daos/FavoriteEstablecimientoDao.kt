package app.regate.data.daos

import app.regate.models.establecimiento.Establecimiento
import app.regate.models.establecimiento.FavoriteEstablecimiento
import kotlinx.coroutines.flow.Flow

interface FavoriteEstablecimientoDao:EntityDao<FavoriteEstablecimiento> {
    fun observeFavoriteEstablecimiento():Flow<List<Establecimiento>>
    fun removeAll()
    fun removeFavoriteEstablecimiento(id:Long)
    fun observeFavoriteEstablecimientosIds():Flow<List<Long>>
}