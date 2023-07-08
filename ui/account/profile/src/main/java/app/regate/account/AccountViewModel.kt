package app.regate.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.auth.AuthRepository
import app.regate.data.common.AddressDevice
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class AccountViewModel(
    observeUser: ObserveUser,
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val appPreferences: AppPreferences,
    observeAuthState: ObserveAuthState
):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val addressDevice = MutableStateFlow<AddressDevice?>(null)
    val state:StateFlow<AccountState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeUser.flow,
        observeAuthState.flow,
        addressDevice
    ){loading,message,user,authState,addressDevice ->
        AccountState(
            loading = loading,
            message = message,
            user = user,
            authState = authState,
            addressDevice = addressDevice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AccountState.Empty
    )
    init {
        observeUser(Unit)
        observeAuthState(Unit)
        viewModelScope.launch {
        appPreferences.observeAddress().collect{
            Log.d("DEBUG_APP","address $it")
            try{
            val address: AddressDevice = Json.decodeFromString(it)
            addressDevice.emit(address)
            Log.d("DEBUG_APP",address.toString())
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
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