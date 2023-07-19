package app.regate.grupos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.interactors.UpdateFilterGrupos
import app.regate.domain.observers.ObserveGrupos
import app.regate.domain.pagination.PaginationGroups
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class GruposViewModel(
    private val grupoRepository: GrupoRepository,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val pagingList:Flow<PagingData<GrupoDto>> = Pager(PAGING_CONFIG){
        PaginationGroups(grupoRepository)
    }.flow.cachedIn(viewModelScope)
//    private val grupos = MutableStateFlow<List<GrupoDto>>(emptyList())
    val state:StateFlow<GruposState> = combine(
//        grupos,
        loadingCounter.observable,
        uiMessageManager.message
    ){loading,message->
        GruposState(
            loading = loading,
            message = message,
//            grupos = grupos
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = GruposState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init{
//        Log.d("DEBUG_APP_222","INIT 222")
//        observeGrupos(Unit)
//        getFilterGrupos()
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
    }