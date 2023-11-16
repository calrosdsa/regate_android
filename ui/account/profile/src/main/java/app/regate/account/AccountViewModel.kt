package app.regate.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.constant.MainPages
import app.regate.constant.Route
import app.regate.data.account.AccountRepository
import app.regate.data.auth.AppAuthState
import app.regate.data.auth.AuthRepository
import app.regate.data.coin.CoinRepository
import app.regate.data.common.AddressDevice
import app.regate.data.system.SystemRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.system.ObserveUnreadNotificationCount
import app.regate.domain.observers.user.ObserveUserProfile
import app.regate.domain.observers.account.ObserveUserBalance
import app.regate.extensions.combine
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class AccountViewModel(
    observeUser: ObserveUserProfile,
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val appPreferences: AppPreferences,
    private val systemRepository: SystemRepository,
    private val coinRepository: CoinRepository,
    observeAuthState: ObserveAuthState,
    observeUserBalance: ObserveUserBalance,
    observeUnreadNotificationCount: ObserveUnreadNotificationCount,
    private val preferences: AppPreferences
):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val addressDevice = MutableStateFlow<AddressDevice?>(null)
//    private val notificationCount = MutableStateFlow(0)
    val state:StateFlow<AccountState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeUser.flow,
        observeAuthState.flow,
        addressDevice,
        observeUserBalance.flow,
        observeUnreadNotificationCount.flow,
    ){loading,message,user,authState,addressDevice,userBalance,unreadNotications ->
        AccountState(
            loading = loading,
            message = message,
            user = user,
            authState = authState,
            addressDevice = addressDevice,
            userBalance = userBalance,
            unreadNotifications = unreadNotications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AccountState.Empty
    )
    init {
        observeUserBalance(Unit)
        observeUnreadNotificationCount(Unit)
        observeUser(Unit)
//        observeUnreadNotificationCount(Unit)
        observeAuthState(Unit)
        viewModelScope.launch {
        appPreferences.observeAddress().collect{
            try{
                val address: AddressDevice = Json.decodeFromString(it)
                addressDevice.emit(address)
                Log.d("DEBUG_APP",address.toString())
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
        }
        viewModelScope.launch {
        observeAuthState.flow.collectLatest{authState:AppAuthState->
            if(authState == AppAuthState.LOGGED_IN){
                getUnreadNotifications()
            }
        }
        }
        getUserBalance()
        updatePreferences()
    }
    private fun updatePreferences(){
        preferences.startRoute = MainPages.Account
    }
    private fun getUnreadNotifications(){
        viewModelScope.launch {
            try{
                systemRepository.getNotifications()
            }catch (e:Exception){
                Log.d("DEBUG_APP_E",e.localizedMessage?:"")
            }
        }
    }
//    fun getNotificationCount(){
//        viewModelScope.launch {
//        try{
//            val res = systemRepository.getNotificationsCount()
//            Log.d("DEBUG_APP_COUNT",res.toString())
//            notificationCount.emit(res.count)
//        }catch (e:Exception){
//            Log.d("DEBUG_APP_COUNT",e.localizedMessage?:"")
//
//            //TODO()
//        }
//        }
//    }
    fun getUserBalance(){
        viewModelScope.launch {
            try{
                coinRepository.getUserBalance()
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.clearAuth()
            accountRepository.clearAuthData()
        }
    }
}