package app.regate.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.constant.MainPages
import app.regate.domain.observers.chat.ObserveUnreadMessagesCount
import app.regate.domain.observers.system.ObserveUnreadNotificationCount
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class InicioViewModel(
    observeUnreadNotificationCount: ObserveUnreadNotificationCount,
    observeUnreadMessagesCount: ObserveUnreadMessagesCount,
    private val preferences: AppPreferences
):ViewModel() {
//    private val loadingCounter = ObservableLoadingCounter()
//    private val messageManager = UiMessageManager()
    val state:StateFlow<InicioState> = combine(
     observeUnreadNotificationCount.flow,
    observeUnreadMessagesCount.flow
    ){notifications,messages->
        InicioState(
            notificationCount = notifications,
            messagesCount = messages
//            loading = loading,
//            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InicioState.Empty
    )

    init{
        observeUnreadMessagesCount(Unit)
        observeUnreadNotificationCount(Unit)
    }

    fun updateInitPage(page:Int) {
        preferences.startRoute =page
    }
}