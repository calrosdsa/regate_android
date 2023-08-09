package app.regate.establecimiento

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import app.regate.api.UiMessageManager
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.interactors.UpdateEstablecimiento
import app.regate.domain.interactors.UpdateEstablecimientoDetail
import app.regate.domain.observers.ObserveEstablecimientoDetail
import app.regate.domain.observers.ObserveInstalacionCategoryCount
import app.regate.domain.observers.ObserveLabelByIds
import app.regate.extensions.combine
import app.regate.models.LabelType
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


@Inject
class EstablecimientoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeInstalacionCategoryCount: ObserveInstalacionCategoryCount,
    observeEstablecimientoDetail: ObserveEstablecimientoDetail,
    observeAmenities: ObserveLabelByIds,
    observeRules:ObserveLabelByIds,
    private val updateEstablecimiento: UpdateEstablecimientoDetail,
    private val establecimientoRepository:EstablecimientoRepository

//    private val salaRepository: SalaRepository
):ViewModel(){
    private val establecimientoId: Long = checkNotNull(savedStateHandle["id"]!!)
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val isFavorite  = MutableStateFlow(false)

    val state:StateFlow<EstablecimientoState> = combine(
        observeEstablecimientoDetail.flow,
        observeInstalacionCategoryCount.flow,
        observeAmenities.flow,
        observeRules.flow,
        loadingState.observable,
        uiMessageManager.message,
        isFavorite,
    ){establecimiento,instalacionCategoryCount,amenities,rules,loading,message,isFavorite->
        EstablecimientoState(
            loading = loading,
            message = message,
            establecimiento = establecimiento,
            instalacionCategoryCount = instalacionCategoryCount,
            amenities = amenities,
            rules = rules,
            isFavorite = isFavorite

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientoState.Empty
    )


    init{
        viewModelScope.launch {
        observeEstablecimientoDetail.flow.collect{
            observeAmenities(ObserveLabelByIds.Params(LabelType.AMENITIES,it.amenities))
            observeRules(ObserveLabelByIds.Params(LabelType.RULES,it.rules))
        }
        }
        observeEstablecimientoDetail(ObserveEstablecimientoDetail.Params(establecimientoId))
        observeInstalacionCategoryCount(ObserveInstalacionCategoryCount.Params(establecimientoId,LabelType.CATEGORIES))
        getStablecimiento()
        checkIsFavorite()
    }
    fun checkIsFavorite(){
        viewModelScope.launch {
        try{
            establecimientoRepository.checkIsFavorite().collectLatest {
                if(it.contains(establecimientoId)){
                    isFavorite.tryEmit(true)
                }
            }
        }catch(e:Exception){
            //TODO()
        }
        }
    }
    fun like(){
        viewModelScope.launch {
            try{
            establecimientoRepository.likeEstablecimiento(establecimientoId)
                isFavorite.tryEmit(true)
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
    fun removeLike(){
        viewModelScope.launch {
            try{
                establecimientoRepository.removeLikeEstablecimiento(establecimientoId)
                isFavorite.tryEmit(false)
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
    fun getStablecimiento(){
        viewModelScope.launch {
            updateEstablecimiento(
                UpdateEstablecimientoDetail.Params(establecimientoId)
            ).collectStatus(loadingState,uiMessageManager)
        }
    }

    fun setCategory(){
        viewModelScope.launch {
//            val category = savedStateHandle.get<Long>("category_id")
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
    fun getId():Long{
        return establecimientoId
    }
}