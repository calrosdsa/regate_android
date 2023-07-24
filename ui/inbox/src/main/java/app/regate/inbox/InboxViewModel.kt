package app.regate.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.conversation.ConversationState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class InboxViewModel(

):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<ConversationState>  = combine(
        loadingCounter.observable,
        uiMessageManager.message
    ){loading,message->
        ConversationState(
            loading = loading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InboxState.Empty
    )
}