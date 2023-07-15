package app.regate.usergroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.interactors.UpdateFilterGrupos
import app.regate.domain.observers.ObserveGrupos
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupsViewModel(
    observeGrupos: ObserveGrupos,
//    private val grupoRepository: GrupoRepository,
    private val updateFilterGrupos: UpdateFilterGrupos
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

    init{
        observeGrupos(Unit)
        getFilterGrupos()
    }

    fun getFilterGrupos(){
        viewModelScope.launch {
//            try{
//               grupoRepository.filterGrupos(FilterGrupoData(category_id = 1))
//            }catch (e:ResponseException){
//              Log.d("DEBUG_APP",e.response.body<String>().toString())
//            } catch (e:Exception){
//                Log.d("DEBUG_APP",e.localizedMessage?:"")
//            }
            updateFilterGrupos(UpdateFilterGrupos.Params(d = FilterGrupoData(category_id = 1)))
                .collectStatus(loadingCounter,uiMessageManager)
        }
    }
    }