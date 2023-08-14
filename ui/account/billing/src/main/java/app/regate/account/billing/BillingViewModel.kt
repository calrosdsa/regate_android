package app.regate.account.billing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class BillingViewModel(
    private val coinRepository: CoinRepository
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageMananger = UiMessageManager()
    private val balance = MutableStateFlow<UserBalance?>(null)
    val state:StateFlow<BillingState> = combine(
        loadingCounter.observable,
        uiMessageMananger.message,
        balance
    ){loading,message,balance->
        BillingState(
            loading = loading,
            message = message,
            balance = balance
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = BillingState.Empty
    )

    init {
        getUserBalance()
    }

    private fun getUserBalance(){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
                val res = coinRepository.getUserBalance()
                balance.emit(res)
                loadingCounter.removeLoader()
            }catch(e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }
}