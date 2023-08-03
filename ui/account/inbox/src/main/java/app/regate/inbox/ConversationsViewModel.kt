package app.regate.inbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.coin.ConversationRepository
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class ConversationsViewModel(
    private val conversationRepository:ConversationRepository
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val state:StateFlow<ConversationsState>  = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        conversations
    ){loading,message,conversations->
        ConversationsState(
            loading = loading,
            message = message,
            conversations = conversations
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ConversationsState.Empty
    )

    init {
//        Log.d("DEBUG_APP",establecimientoId.toString())
        getConversations()
    }
    fun getConversations(){
        viewModelScope.launch {
            try{
                val res = conversationRepository.getConversations()
                conversations.tryEmit(res)
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
            }
        }
    }
}