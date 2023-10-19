package app.regate.data.daos

import app.regate.compoundmodels.UserProfileGrupo
import app.regate.compoundmodels.UserProfileSala
import app.regate.models.UserSala
import kotlinx.coroutines.flow.Flow

interface UserSalaDao:EntityDao<UserSala> {
    fun observeUsersSala(id:Long): Flow<List<UserProfileSala>>
}