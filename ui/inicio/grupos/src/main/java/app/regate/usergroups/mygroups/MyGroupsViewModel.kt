package app.regate.usergroups.mygroups

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObserveUserGroups
import app.regate.models.Grupo
import app.regate.util.ObservableLoadingCounter
import app.regate.util.getLongUuid
import app.regate.util.now
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class MyGroupsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeGrupos: ObserveUserGroups,
    observeUser: ObserveUser,
    private val grupoRepository: GrupoRepository,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val data = savedStateHandle.get<String>("data") ?: ""
    private val loadingCounter = ObservableLoadingCounter()
    private val selectedGroups = MutableStateFlow<List<Grupo>>(emptyList())
    private val uiMessageManager = UiMessageManager()
    val state: StateFlow<MyGroupsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message,
        selectedGroups,
        observeUser.flow
    ) { grupos, loading, message,selectedGroups,user ->
        MyGroupsState(
            loading = loading,
            message = message,
            grupos = grupos,
            selectedGroups = selectedGroups,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MyGroupsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeGrupos(Unit)
        getUserGrupos()
        observeUser(Unit)
    }
    fun selectGroup(id:Long) {
        viewModelScope.launch {
            try {
                if (selectedGroups.value.map { it.id }.contains(id)) {
                    val newList = selectedGroups.value.filter { it.id != id }
                    selectedGroups.emit(newList)
                } else {
                    val newItem = state.value.grupos.find { it.id == id }
                    if(newItem != null){
                        val items = selectedGroups.value + newItem
                        selectedGroups.emit(items)
                    }
                }
            } catch (e: Exception) {
                Log.d("DEBUG_APP", e.localizedMessage ?: "")
            }
        }
    }
    fun getUserGrupos() {
        viewModelScope.launch {
            try {
                grupoRepository.myGroups()
            } catch (e: Exception) {
                Log.d("DEBUG_APP_!21", e.localizedMessage ?: "")
            }
        }
    }

    fun navigateToGroupChat(groupId: Long,context:Context,content:String,navigate: (Long, String) -> Unit,
    navigateUp:()->Unit) {
        try {
            if (data.isNotBlank()) {
                val grupoMessageData = Json.decodeFromString<GrupoMessageData>(data)
                val newData = Json.encodeToString(grupoMessageData.copy(content = content))
                if (selectedGroups.value.size > 1) {
                    val messages = mutableListOf<GrupoMessageDto>()
                    selectedGroups.value.map {grupo->
                        val message = state.value.user?.let {
                            GrupoMessageDto(
                                id = getLongUuid(),
                                content = content,
                                data = grupoMessageData.data,
                                type_message = grupoMessageData.type_data,
                                created_at = now(),
                                chat_id = grupo.id,
                                profile_id = it.profile_id
                            )
                        }
                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    viewModelScope.launch {
                        try {
                            grupoRepository.sendShareMessage(messages)
                            Toast.makeText(context,"Mensages enviados...",Toast.LENGTH_SHORT).show()
                            delay(2000)
                            navigateUp()
                        } catch (e: Exception) {
                            Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
                        }
                    }
                } else {
                    navigate(groupId, newData)
                }
            }
        } catch (e: Exception) {
            Log.d("DEBUG_APP", e.localizedMessage ?: "")
        }
    }
    fun getData():String{
        return data
    }

}