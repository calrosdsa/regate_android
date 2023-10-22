package app.regate.usergroups

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveUserGroupsWithMessage
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupsViewModel(
    observeGrupos: ObserveUserGroupsWithMessage,
    private val grupoRepository: GrupoRepository,
    observeAuthState: ObserveAuthState,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<UserGroupsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){grupos,loading,message,authState->
        UserGroupsState(
            loading = loading,
            message = message,
            grupos = grupos,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = UserGroupsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeAuthState(Unit)
        observeGrupos(Unit)
        getUserGrupos()
        viewModelScope.launch {
            observeGrupos.flow.collectLatest{
                Log.d("DEBUG_APP_G",it.toString())
            }
        }
    }

    fun getUserGrupos(){
        viewModelScope.launch {
            try{
            grupoRepository.myGroups()
            grupoRepository.myGroupsRequest()
            }catch(e:Exception){
                Log.d("DEBUG_APP_!21",e.localizedMessage?:"")
            }
        }
    }
    }