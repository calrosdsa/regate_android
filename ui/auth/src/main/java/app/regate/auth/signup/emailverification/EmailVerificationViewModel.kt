package app.regate.auth.signup.emailverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.domain.observers.ObserveUser
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class EmailVerificationViewModel(
    observeUser: ObserveUser,
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<EmailVerificationState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeUser.flow,
    ){loading,message,user->
        EmailVerificationState(
            loading = loading,
            message = message,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = EmailVerificationState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeUser(Unit)
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}