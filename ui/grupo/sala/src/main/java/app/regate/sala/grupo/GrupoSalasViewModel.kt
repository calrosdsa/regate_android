package app.regate.sala.grupo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.sala.SalaRepository
import app.regate.domain.Converter
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationSalaFilter
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class GrupoSalasViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val salaRepository: SalaRepository,
    observeAuthState: ObserveAuthState,
//    private val converter:Converter
):ViewModel() {
    private val grupoId = savedStateHandle.get<Long>("id") ?: 0
    private val loadingCounter = ObservableLoadingCounter()
    val pagedList: Flow<PagingData<SalaDto>> = Pager(PAGING_CONFIG){
        PaginationSalaFilter(isInit = true){page->
            salaRepository.getGrupoSalas(grupoId,page)
//            salaRepository.filterSalas()
        }
    }.flow.cachedIn(viewModelScope)
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<GrupoSalasState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow,
    ){loading,message,authState->
        GrupoSalasState(
            loading = loading,
            message=  message,
            authState = authState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GrupoSalasState.Empty
    )

    init{
        observeAuthState(Unit)
//        setCategories()
    }
    fun getGrupoId():Long {
        return grupoId
    }

//    fun setCategories(){
//        viewModelScope.launch {
//            try{
//
//            val categories = converter.getCategories()
//           filterData.tryEmit(filterData.value.copy(
//                isInit = true,
//                categories = categories
//            ))
//            }catch(e:Exception){
//                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
//            }
//        }
//    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )

    }
}