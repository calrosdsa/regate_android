package app.regate.domain.observers

import app.regate.data.auth.AppAuthState
import app.regate.data.auth.AuthRepository
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAuthState(
    private val AuthRepository: AuthRepository,
) : SubjectInteractor<Unit, AppAuthState>() {
    override fun createObservable(params: Unit): Flow<AppAuthState> {
        return AuthRepository.state
    }
}
