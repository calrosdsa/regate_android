package app.regate.account.billing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.empresa.coin.UserBalanceDto
import app.regate.domain.observers.account.ObserveUserBalance
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
    private val coinRepository: CoinRepository,
    observeUserBalance:ObserveUserBalance,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageMananger = UiMessageManager()
    val state:StateFlow<BillingState> = combine(
        loadingCounter.observable,
        uiMessageMananger.message,
        observeUserBalance.flow,
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
        observeUserBalance(Unit)
    }

    private fun getUserBalance(){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
                coinRepository.getUserBalance()
                loadingCounter.removeLoader()
            }catch(e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }
}