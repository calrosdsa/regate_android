package app.regate.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class FavoritesViewModel(
    private val establecimientoRepository: EstablecimientoRepository
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<FavoritesState> = combine(
        loadingCounter.observable,
        uiMessageManager.message
    ){loading,message->
        FavoritesState(
            loading = loading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FavoritesState.Empty
    )
    init {
        getFavoriteEstablecimientos()
    }

    fun getFavoriteEstablecimientos(){
        viewModelScope.launch {
            try{
                establecimientoRepository.getFavoritosEstablecimiento()
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }
}