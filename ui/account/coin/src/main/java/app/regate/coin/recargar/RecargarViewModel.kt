package app.regate.coin.recargar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.domain.observers.ObserveAuthState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.days

@Inject
class RecargarViewModel(
    observeAuthState: ObserveAuthState,
    private val coinRepository: CoinRepository
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val coins = MutableStateFlow<List<RecargaCoinDto>>(emptyList())
    val state:StateFlow<RecargarState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        coins,
        observeAuthState.flow
    ){loading,message,coins,authState->
        RecargarState(
            loading = loading,
            message = message,
            coins = coins,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RecargarState.Empty
    )

    init {
        observeAuthState(Unit)
        getCoins()
    }
    fun navigateToPayScreen(amount:String):String?{
        return try {
            val monto = amount.toInt()
            if(monto <= 0 ) {
                viewModelScope.launch {
                    uiMessageManager.emitMessage(UiMessage(message = "Monto invalido"))
                }
                return null
            }
            val expirationDate = (Clock.System.now()+ 3.days).toLocalDateTime(TimeZone.UTC).date.toString()
            val qrRequest = QrRequest(
                currency = "BOB",
                gloss = "Compra de monedas (Regate).",
                amount = monto,
                singleUse = true,
                expirationDate = expirationDate,
                additionalData = "",
                destinationAccountId = "1"
            )

             Json.encodeToString(qrRequest)
        } catch (e: Exception) {
            viewModelScope.launch {
                uiMessageManager.emitMessage(UiMessage(message = "Monto invalido"))
            }
            Log.d("DEBUG_APP_", e.cause?.localizedMessage ?: "")
            Log.d("DEBUG_APP_", e.localizedMessage ?: "")
            null
        }
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

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}