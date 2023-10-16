package app.regate.entidad.photos

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.PhotoDto
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
class EstablecimientoPhotosViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val establecimientoRepository: EstablecimientoRepository

//    private val appDateFormatter: AppDateFormatter,
):ViewModel(){
    private val id: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val photos = MutableStateFlow<List<PhotoDto>>(emptyList())

    val state: StateFlow<EstablecimientoPhotoState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        photos,
    ){loading,message,photos ->
        EstablecimientoPhotoState(
            loading = loading,
            message = message,
            photos = photos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientoPhotoState.Empty
    )

    init {
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
        try {
            val res = establecimientoRepository.getEstablecimientoPhotos(id)
            photos.emit(res)
        }catch (e:Exception){
            Log.d("DEBUG_APP",e.localizedMessage?:"")
        }
        }
    }



    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}