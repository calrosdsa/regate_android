package app.regate.sala.complete

import app.regate.common.resources.R
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.CompleteSalaRequest
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.sala.SalaRepository
import app.regate.domain.observers.ObserveUser
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SalaCompleteViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeUser: ObserveUser,
    private val salaRepository: SalaRepository,
):ViewModel() {
    private val salaId = savedStateHandle.get<Long>("id")?:0
    private val loadingConter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val salaCompleteHistory = MutableStateFlow<SalaCompleteDetail?>(null)
    val state :StateFlow<SalaCompleteState> = combine(
        loadingConter.observable,
        uiMessageManager.message,
        salaCompleteHistory,
        observeUser.flow,
    ){loading,message,salaCompleteHistory,user->
        SalaCompleteState(
            loading = loading,
            message = message,
            salaCompleteDetail = salaCompleteHistory,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SalaCompleteState.Empty
    )

    init {
        observeUser(Unit)
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            try{
                val res = salaRepository.getSalaCompleteHistory(salaId)
                salaCompleteHistory.emit(res)
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
    fun completeSala(context:Context,amount:String){
        viewModelScope.launch {
            try {
                loadingConter.addLoader()
                val data = CompleteSalaRequest(
                    sala_id = salaId,
                    amount = amount.replace(",",".").toDouble()
                )
                loadingConter.removeLoader()
                salaRepository.salaComplete(data)
                getData()
            }catch (e:ResponseException){
                loadingConter.removeLoader()
                if(e.response.status == HttpStatusCode.BadRequest){
                  uiMessageManager.emitMessage(UiMessage(
                      message= e.response.body<ResponseMessage>().message
                  ))
              }else{
                    uiMessageManager.emitMessage(UiMessage(
                        message= context.getString(R.string.unexpected_error)
                    ))
                }
                Log.d("DEBUG_APP",e.localizedMessage?:"")

            } catch (e:Exception){
                loadingConter.removeLoader()
                Log.d("DEBUG_APP",e.localizedMessage?:"")
                uiMessageManager.emitMessage(UiMessage(
                    message= context.getString(R.string.unexpected_error)
                ))
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}