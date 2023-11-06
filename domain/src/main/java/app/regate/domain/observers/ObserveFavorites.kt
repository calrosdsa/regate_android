package app.regate.domain.observers

import app.regate.data.daos.FavoriteEstablecimientoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.establecimiento.Establecimiento
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveFavorites (
    private val favoriteEstablecimientoDao: FavoriteEstablecimientoDao
    ):SubjectInteractor<Unit,List<Establecimiento>>(){
    override fun createObservable(params: Unit): Flow<List<Establecimiento>> {
        return favoriteEstablecimientoDao.observeFavoriteEstablecimiento()
    }
}