package app.regate.createsala.establecimiento

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.util.ObservableLoadingCounter
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
    private val establecimientoRepository: EstablecimientoRepository
):ViewModel() {
    val loadingCounter = ObservableLoadingCounter()
    private val grupoId = savedStateHandle.get<Long>("grupo_id")
    val uiMessageManager = UiMessageManager()
    val establecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    val state:StateFlow<EstablecimientoFilterState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        establecimientos
    ){loading,message,establecimientos ->
        EstablecimientoFilterState(
            loading = loading,
            message = message,
            establecimientos = establecimientos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientoFilterState.Empty
    )
    init {
        getEstablecimientos()
    }

    private fun getEstablecimientos(){
        viewModelScope.launch {
            try{
                val res = establecimientoRepository.getEstablecimientos()
                establecimientos.tryEmit(res)
            }catch(e:Exception){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }

    fun geIdGroup():Long?{
        return grupoId
    }
}