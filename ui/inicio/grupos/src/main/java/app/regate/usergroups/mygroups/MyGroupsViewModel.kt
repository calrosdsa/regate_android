package app.regate.usergroups.mygroups

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveUserGroups
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class MyGroupsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeGrupos: ObserveUserGroups,
    private val grupoRepository: GrupoRepository,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val data = savedStateHandle.get<String>("data") ?: ""
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state: StateFlow<MyGroupsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message
    ) { grupos, loading, message ->
        MyGroupsState(
            loading = loading,
            message = message,
            grupos = grupos
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MyGroupsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeGrupos(Unit)
        getUserGrupos()
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

    fun navigateToGroupChat(groupId: Long, navigate: (Long, String) -> Unit) {
        try {
            if (data.isNotBlank()) {
                navigate(groupId, data)
            }
        } catch (e: Exception) {
            Log.d("DEBUG_APP", e.localizedMessage ?: "")
        }
    }

}