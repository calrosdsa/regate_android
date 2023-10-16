package app.regate.establecimiento

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.interactors.UpdateEstablecimientoDetail
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.establecimiento.ObserveEstablecimientoDetail
import app.regate.domain.observers.instalacion.ObserveInstalacionCategoryCount
import app.regate.domain.observers.establecimiento.ObserveAttentionSchedule
import app.regate.domain.observers.establecimiento.ObserveAttentionScheduleWeek
import app.regate.domain.observers.labels.ObserveLabelByIds
import app.regate.extensions.combine
import app.regate.models.LabelType
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
    observeRules: ObserveLabelByIds,
    observeAuthState: ObserveAuthState,
    observeAttentionSchedule:ObserveAttentionSchedule,
    observeAttentionScheduleWeek: ObserveAttentionScheduleWeek,
    private val updateEstablecimiento: UpdateEstablecimientoDetail,
//    private val updateInstalaciones:UpdateInstalaciones,
    private val establecimientoRepository:EstablecimientoRepository

//    private val salaRepository: SalaRepository
):ViewModel(){
    private val establecimientoId: Long = checkNotNull(savedStateHandle["id"]!!)
    private val dayWeek = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek.ordinal+1
    private val loadingState = ObservableLoadingCounter()
    private val loadingState2 = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val isFavorite  = MutableStateFlow(false)
    private val reviews = MutableStateFlow<EstablecimientoReviews?>(null)
    val secondState:StateFlow<SecondState> = combine(
        loadingState2.observable,
        observeAttentionScheduleWeek.flow
    ) {loading,attentionScheduleWeek->
        SecondState(
            loading = loading,
            attentionScheduleWeek = attentionScheduleWeek,
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = SecondState.Empty,
        started = SharingStarted.Lazily
    )
    val state:StateFlow<EstablecimientoState> = combine(
        observeEstablecimientoDetail.flow,
        observeInstalacionCategoryCount.flow,
        observeAmenities.flow,
        observeRules.flow,
        observeAuthState.flow,
        observeAttentionSchedule.flow,
        loadingState.observable,
        uiMessageManager.message,
        isFavorite,
        reviews
    ){establecimiento,instalacionCategoryCount,amenities,rules,authState,
      attentionSchedule,loading,message,isFavorite,reviews->
        EstablecimientoState(
            loading = loading,
            message = message,
            establecimiento = establecimiento,
            instalacionCategoryCount = instalacionCategoryCount,
            amenities = amenities,
            rules = rules,
            isFavorite = isFavorite,
            reviews=  reviews,
            authState = authState,
            attentionSchedule = attentionSchedule,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EstablecimientoState.Empty
    )


    init{
        viewModelScope.launch {
        getStablecimiento()
        observeEstablecimientoDetail.flow.collect{establecimiento->
            try{
            Log.d("DEBUG_APP",establecimiento.toString())
            observeAmenities(ObserveLabelByIds.Params(LabelType.AMENITIES,establecimiento.amenities))
            observeRules(ObserveLabelByIds.Params(LabelType.RULES,establecimiento.rules))
            observeInstalacionCategoryCount(ObserveInstalacionCategoryCount.Params(establecimientoId,LabelType.CATEGORIES))
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
        }
        observeEstablecimientoDetail(ObserveEstablecimientoDetail.Params(establecimientoId))
        observeAttentionSchedule(ObserveAttentionSchedule.Params(establecimientoId,dayWeek))
        observeAttentionScheduleWeek(ObserveAttentionScheduleWeek.Params(establecimientoId))
        observeAuthState(Unit)
        getReviews()
        checkIsFavorite()
    }
    private fun checkIsFavorite(){
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
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }
    fun removeLike(){
        viewModelScope.launch {
            try{
                establecimientoRepository.removeLikeEstablecimiento(establecimientoId)
                isFavorite.tryEmit(false)
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }
    private fun getReviews(){
        viewModelScope.launch {
                try{
                    Log.d("DEBUG_APP",establecimientoId.toString())
                    val res =
                        establecimientoRepository.getEstablecimientoReviews(establecimientoId, 1,5)
                    Log.d("DEBUG_APP",res.toString())
                    reviews.emit(res)
                }catch (e:Exception){
                    Log.d("DEBUG_APP",e.localizedMessage?:"")
                }
            }
    }
    fun getStablecimiento(){
        viewModelScope.launch {
//            Log.d("DEBUG_APP_DAY",dayWeek.name)
            updateEstablecimiento(
                UpdateEstablecimientoDetail.Params(establecimientoId,dayWeek)
            ).collectStatus(loadingState,uiMessageManager)                                           
        }
    }
    fun updateScheduleWeek(){
        viewModelScope.launch {
            try{
                loadingState2.addLoader()
                establecimientoRepository.updateAttentionScheduleWeek(establecimientoId)
                loadingState2.removeLoader()
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
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