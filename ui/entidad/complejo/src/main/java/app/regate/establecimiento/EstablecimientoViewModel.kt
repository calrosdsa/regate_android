package app.regate.establecimiento

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import app.regate.api.UiMessageManager
import app.regate.domain.interactors.UpdateEstablecimiento
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
    private val updateEstablecimiento: UpdateEstablecimiento,

//    private val salaRepository: SalaRepository
):ViewModel(){
    private val establecimientoId: Long = checkNotNull(savedStateHandle["id"]!!)
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state:StateFlow<EstablecimientoState> = combine(
        observeEstablecimientoDetail.flow,
        observeInstalacionCategoryCount.flow,
        observeAmenities.flow,
        observeRules.flow,
        loadingState.observable,
        uiMessageManager.message,
    ){establecimiento,instalacionCategoryCount,amenities,rules,loading,message->
        EstablecimientoState(
            loading = loading,
            message = message,
            establecimiento = establecimiento,
            instalacionCategoryCount = instalacionCategoryCount,
            amenities = amenities,
            rules = rules

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
    }
    fun getStablecimiento(){
        viewModelScope.launch {
            updateEstablecimiento(
                UpdateEstablecimiento.Params(establecimientoId)
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