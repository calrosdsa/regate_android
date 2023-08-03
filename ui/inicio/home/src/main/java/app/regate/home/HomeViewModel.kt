package app.regate.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.account.AccountRepository
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.sala.SalaRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.extensions.combine
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private val salaRepository: SalaRepository,
    private val preferences: AppPreferences,
//    authStore: AuthStore,
    observeAuthState: ObserveAuthState,
    ):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val establecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    private val recommended = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    private val nearEstablecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    private val salas = MutableStateFlow<List<SalaDto>>(emptyList())
    //    val token  =authStore.get()?.let {
//        it.accessToken
//    }
    val state:StateFlow<HomeState> = combine(
        loadingState.observable,
        establecimientos,
        observeAuthState.flow,
        recommended,
        nearEstablecimientos,
        salas,
    ){loading,establecimientos,authState,recommended,near,salas->
        HomeState(
            loading = loading,
            establecimientos = establecimientos,
            authState = authState,
            recommended = recommended,
            nearEstablecimientos = near,
            salas = salas
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeState.Empty
    )

    init{


        me()
        getEstablecimientos()
        observeAuthState(Unit)
    }
//    fun getReccomendedEstablecimientos(){
//    }
    fun getEstablecimientos(){
        viewModelScope.launch {
            try{
                val categories = Json.decodeFromString<List<Long>>(preferences.categories)
                loadingState.addLoader()
                val res = async { establecimientoRepository.getEstablecimientos() }
                val recommendedRes= async { establecimientoRepository.getRecommendedEstablecimientos(categories) }
//                val salaRes = async { salaRepository.filterSalas(SalaFilterData(categories = categories)) }
                delay(1000)
                Log.d("API_REQUEST",res.toString())
                establecimientos.tryEmit(res.await())
                recommended.tryEmit(recommendedRes.await())
//                salas.tryEmit(salaRes.await())
                loadingState.removeLoader()
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