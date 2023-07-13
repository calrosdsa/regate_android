package app.regate.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.account.AccountRepository
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import java.util.UUID

@Inject
class HomeViewModel(
    private val accountRepository: AccountRepository,
    private val establecimientoRepository: EstablecimientoRepository,
//    preferences: AppPreferences,
//    authStore: AuthStore,
    observeAuthState: ObserveAuthState,
    ):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val establecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
//    val token  =authStore.get()?.let {
//        it.accessToken
//    }
    val state:StateFlow<HomeState> = combine(
        loadingState.observable,
        establecimientos,
        observeAuthState.flow
    ){loading,establecimientos,authState->
        HomeState(
            loading = loading,
            establecimientos = establecimientos,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeState.Empty
    )

    init{
//        viewModelScope.launch {
//        authStore.get()?.let {
//            Log.d("TOKEN_",it.accessToken)
//        }
//        }
//        try{

//        Log.d("DEBUG_APP",UUID.randomUUID().leastSignificantBits.toString())
//            Log.d("DEBUG_APP",UUID.randomUUID().mostSignificantBits.toString())
//            Log.d("DEBUG_APP", (UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE).toString()
//            )

//        }catch(e:Exception){
//            Log.d("DEBUG_APP",e.localizedMessage?:"")
//
//        }

        me()
        getEstablecimientos()
        observeAuthState(Unit)
    }

    fun getEstablecimientos(){
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                val res = establecimientoRepository.getEstablecimientos()
                delay(1000)
                Log.d("API_REQUEST",res.toString())
                loadingState.removeLoader()
                establecimientos.tryEmit(res)
            }catch(e:Exception){
                loadingState.removeLoader()
                Log.d("API_REQUEST",e.localizedMessage?:"None")
            }
        }
    }

    fun me(){
        viewModelScope.launch {
        try{
            val me  = accountRepository.me()
            Log.d("API_",me.toString())
        }catch(e:Exception){
            Log.d("API_",e.localizedMessage?:"None")
        }
        }
    }
}