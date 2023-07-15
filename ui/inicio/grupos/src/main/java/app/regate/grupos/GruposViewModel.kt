package app.regate.grupos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.interactors.UpdateFilterGrupos
import app.regate.domain.observers.ObserveGrupos
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class GruposViewModel(
    observeGrupos: ObserveGrupos,
    private val grupoRepository: GrupoRepository,
    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<GruposState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message
    ){grupos,loading,message->
        GruposState(
            loading = loading,
            message = message,
            grupos = grupos
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = GruposState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init{
        observeGrupos(Unit)
        getFilterGrupos()
    }

    fun getFilterGrupos(){
        viewModelScope.launch {
            try{
               grupoRepository.filterGrupos(FilterGrupoData(category_id = 1))
            }catch (e:ResponseException){
              Log.d("DEBUG_APP",e.response.body<String>().toString())
            } catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
//            updateFilterGrupos(UpdateFilterGrupos.Params(d = FilterGrupoData(category_id = 1)))
//                .collectStatus(loadingCounter,uiMessageManager)
        }
    }
    }