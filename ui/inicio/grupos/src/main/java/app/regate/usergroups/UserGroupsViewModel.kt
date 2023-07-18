package app.regate.usergroups

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveUser
import app.regate.domain.observers.ObserveUserGroups
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupsViewModel(
    observeGrupos: ObserveUserGroups,
    private val grupoRepository: GrupoRepository,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<UserGroupsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message
    ){grupos,loading,message->
        UserGroupsState(
            loading = loading,
            message = message,
            grupos = grupos
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = UserGroupsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeGrupos(Unit)
        getUserGrupos()
    }

    fun getUserGrupos(){
        viewModelScope.launch {
            try{
            grupoRepository.myGroups()
            }catch(e:Exception){
                Log.d("DEBUG_APP_!21",e.localizedMessage?:"")
            }
        }
    }
    }