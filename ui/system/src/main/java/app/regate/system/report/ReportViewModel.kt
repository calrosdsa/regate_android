package app.regate.system.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.auth.AuthRepository
import app.regate.data.dto.system.ReportData
import app.regate.data.system.SystemRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.util.Locale

@Inject
class ReportViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val systemRepository: SystemRepository
    ):ViewModel() {
    private val data = savedStateHandle.get<String>("data")
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<ReportState> = combine(
        loadingState.observable,
        uiMessageManager.message,
    ){loading,message ->
        ReportState(
            loading = loading,
            message = message

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReportState.Empty
    )
    init {
       Log.d("DEBUG_APP",data.toString())
    }

    fun sendReport(detail:String){
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                data?.let {
                    val report  = Json.decodeFromString<ReportData>(it)
                    systemRepository.sendReport(report.copy(
                        detail = detail
                    ))
                }
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(
                    message = "Se ha enviado su reporte"
                ))
            }catch (e:Exception){
                loadingState.removeLoader()
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}