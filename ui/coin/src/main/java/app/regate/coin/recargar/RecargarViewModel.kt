package app.regate.coin.recargar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class RecargarViewModel(
    private val coinRepository: CoinRepository
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val coins = MutableStateFlow<List<RecargaCoinDto>>(emptyList())
    val state:StateFlow<RecargarState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        coins,
    ){loading,message,coins->
        RecargarState(
            loading = loading,
            message = message,
            coins = coins
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RecargarState.Empty
    )

    init {
        getCoins()
    }

    fun getCoins(){
        viewModelScope.launch {
            try{
                val res = coinRepository.getRecargaCoins()
                coins.tryEmit(res)
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
                //TODO()
            }
        }
    }
}