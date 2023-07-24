package app.regate.conversation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.inbox.InboxState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ConversationViewModel(
    @Assisted savedStateHandle: SavedStateHandle
):ViewModel() {
    private val establecimientoId = savedStateHandle.get<Long>("id")?:0
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

    init {
        Log.d("DEBUG_APP",establecimientoId.toString())
    }
}