package app.regate.map.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.handleApi
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.util.ObservableLoadingCounter
import coil.load
import coil.request.CachePolicy
import coil.request.Disposable
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class EstablecimientosMapViewModel(
    private val client:HttpClient
//    private val establecimientoRepository:EstablecimientoRepository
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val establecimientos = MutableStateFlow<List<EstablecimientoDto>>(emptyList())
    val textValue = "EXAMPLE FOR VIEWModel"
    val state:StateFlow<EstablecimientosMapState> = combine(
        loadingState.observable,
        establecimientos
    ){loading,establecimientos->
        EstablecimientosMapState(
            loading = loading,
            establecimientos = establecimientos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientosMapState.Empty
    )

    init {
        getEstablecimientos()
    }
    fun observeState(): StateFlow<EstablecimientosMapState> {
        return state
    }

    fun loadImageFromUrl(url:String,img:ImageView?) {
        img?.load(url) {
            placeholder(app.regate.common.resources.R.drawable.image)
            transformations(CircleCropTransformation())
            // if we want rounded corners
            transformations(RoundedCornersTransformation())
            // enable network caching
            networkCachePolicy(CachePolicy.ENABLED)
            // enable caching on disk
            diskCachePolicy(CachePolicy.ENABLED)
            // enable memory caching
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }

    fun getEstablecimientos(){
        viewModelScope.launch {
            try{
                val res:List<EstablecimientoDto> =  client.get("/v1/establecimientos/").body()
                establecimientos.emit(res)
                Log.d("DEBUG_APP",res.toString())
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
//        viewModelScope.launch {
//            try{
//                val res = establecimientoRepository.getEstablecimientos()
//                Log.d("API_REQUEST",res.toString())
//                establecimientos.tryEmit(res)
//            }catch(e:Exception){
//                Log.d("API_REQUEST",e.localizedMessage?:"None")
//            }
//        }
    }


}