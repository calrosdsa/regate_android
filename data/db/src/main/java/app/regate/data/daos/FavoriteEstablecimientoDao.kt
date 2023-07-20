package app.regate.data.daos

import app.regate.models.FavoriteEstablecimiento

interface FavoriteEstablecimientoDao:EntityDao<FavoriteEstablecimiento> {
    fun observeFavoriteEstablecimiento()
}