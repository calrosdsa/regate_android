package app.regate.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.account.AccountRepository
import app.regate.data.common.AddressDevice
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.InitialData
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.sala.SalaRepository
import app.regate.domain.Converter
import app.regate.domain.observers.ObserveAuthState
import app.regate.extensions.combine
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import java.util.UUID

@Inject
class HomeViewModel(
    private val accountRepository: AccountRepository,
    private val establecimientoRepository: EstablecimientoRepository,
    private val converter:Converter,
//    private val salaRepository: SalaRepository,
//    authStore: AuthStore,
    observeAuthState: ObserveAuthState,
    ):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val data = MutableStateFlow<InitialData?>(null)
    val state:StateFlow<HomeState> = combine(
        loadingState.observable,
        data,
        converter.observeAddress(),
        observeAuthState.flow,
    ){loading,data,addressDevice,authState->
        HomeState(
            loading = loading,
            authState = authState,
            data = data,
            addressDevice = addressDevice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeState.Empty
    )

    init{
//        me()
        observeAuthState(Unit)
        viewModelScope.launch {
//            salaRepository.insertSalas()
        converter.observeAddress().collectLatest {
            Log.d("API_REQUEST","$it DATA ------")
            getEstablecimientos(it)
        }
        }
    }
//    fun getReccomendedEstablecimientos(){
//    }
    fun getEstablecimientos(addressDevice: AddressDevice?){
        viewModelScope.launch {
            try{
                val categories = converter.getCategories()
                loadingState.addLoader()
                Log.d("DEBUG_APP_ADD",state.value.addressDevice.toString())

                val initialFilterData = InitialDataFilter(
                    categories = categories?: emptyList(),
                    lng = addressDevice?.longitud.toString(),
                    lat = addressDevice?.latitud.toString(),
                    )
                val res = establecimientoRepository.getEstablecimientos(initialFilterData)
                data.tryEmit(res)
                loadingState.removeLoader()
            }catch(e:Exception){
                loadingState.removeLoader()
                Log.d("API_REQUEST",e.localizedMessage?:e.message?:"Unexpected")
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