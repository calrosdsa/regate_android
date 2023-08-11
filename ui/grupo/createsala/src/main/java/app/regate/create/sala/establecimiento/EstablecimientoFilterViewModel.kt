package app.regate.create.sala.establecimiento

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.Converter
import app.regate.domain.observers.ObserveFavorites
import app.regate.domain.pagination.PaginationEstablecimientos
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
class EstablecimientoFilterViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeFavorites: ObserveFavorites,
    private val establecimientoRepository: EstablecimientoRepository,
    private val converter:Converter
):ViewModel() {
    private val grupoId = savedStateHandle.get<Long>("grupo_id")
    private val loadingCounter = ObservableLoadingCounter()
    private val filterData = MutableStateFlow(FILTER_DATA)
    val pagedList: Flow<PagingData<EstablecimientoDto>> = Pager(PAGING_CONFIG){
        PaginationEstablecimientos(isInit = filterData.value.isInit){page->
            establecimientoRepository.getRecommendedEstablecimientos(filterData.value,page)
//            salaRepository.filterSalas()
        }
    }.flow.cachedIn(viewModelScope)
    private val uiMessageManager = UiMessageManager()
//    val establecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    val state:StateFlow<EstablecimientoFilterState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        filterData,
        observeFavorites.flow,
    ){loading,message,filterData,establecimientosfav ->
        EstablecimientoFilterState(
            loading = loading,
            message = message,
            filterData = filterData,
            establecimientosFav = establecimientosfav
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientoFilterState.Empty
    )
    init {
        observeFavorites(Unit)
        getEstablecimientos()

    }

    private fun getEstablecimientos(){
      viewModelScope.launch {
          try{

          val categories = converter.getCategories()
          val addressDevice = converter.getAddress()
          filterData.tryEmit(
              filterData.value.copy(
                  categories = categories,
                  lng = addressDevice?.longitud.toString(),
                  lat = addressDevice?.latitud.toString(),
                  isInit = true
              )
          )
          }catch (e:Exception){
              Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
          }
      }
    }

    fun geIdGroup():Long?{
        return grupoId
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
        val FILTER_DATA = InitialDataFilter()
    }
}