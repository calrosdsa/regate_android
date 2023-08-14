package app.regate.establecimiento.createreview

import app.regate.common.resources.R
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationReviews
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CreateReviewViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    private val establecimientoRepository: EstablecimientoRepository,
) : ViewModel() {
    private val establecimientoId = savedStateHandle.get<Long>("id")?: 0
    private val  loaderCounter = ObservableLoadingCounter()
    private val establecimientoReview = MutableStateFlow<EstablecimientoReview?>(null)
    private val uiMessageManager = UiMessageManager()

    val state:StateFlow<CreateReviewState> = combine(
        loaderCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow,
        establecimientoReview
    ){loading,message,authState,review->
        CreateReviewState(
            loading = loading,
            message = message,
            authState = authState,
            review=  review
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = CreateReviewState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeAuthState(Unit)
        getReviewUser()
    }
    fun getReviewUser(){
        viewModelScope.launch {
            try{
                val res = establecimientoRepository.getReviewUser(establecimientoId)
                establecimientoReview.emit(res)
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }

    fun createReview(score:Int,review:String,context:Context){
        viewModelScope.launch {
            try{
                loaderCounter.addLoader()
                val data = EstablecimientoReview(
                    score = score,
                    review = review,
                    establecimiento_id = establecimientoId
                )
                val res = establecimientoRepository.createEstablecimientoReview(data)
                delay(1000)
                loaderCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(
                    message = context.getString(R.string.successfully_published)
                ))
                Log.d("DEBUG_APP",res.toString())
            }catch (e:Exception){
                loaderCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(
                    message = context.getString(R.string.unexpected_error)
                ))
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}