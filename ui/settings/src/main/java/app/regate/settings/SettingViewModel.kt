package app.regate.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.domain.observers.ObserveUser
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class SettingViewModel(
    observeUser: ObserveUser
):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<SettingState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeUser.flow,
    ){loading,message,user ->
        SettingState(
            loading = loading,
            message = message,
            user = user,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SettingState.Empty
    )
    init {
        observeUser(Unit)
    }
}