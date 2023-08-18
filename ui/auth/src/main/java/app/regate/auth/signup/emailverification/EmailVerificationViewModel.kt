package app.regate.auth.signup.emailverification

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.dto.ResponseMessage
import app.regate.domain.observers.ObserveUser
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

@Inject
class EmailVerificationViewModel(
    observeUser: ObserveUser,
    private val accountRepository: AccountRepository,
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<EmailVerificationState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeUser.flow,
    ){loading,message,user->
        EmailVerificationState(
            loading = loading,
            message = message,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = EmailVerificationState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeUser(Unit)
    }
    fun verifyEmail(otp:String,navigate:()->Unit,context:Context){
        viewModelScope.launch {
        try{
            loadingCounter.addLoader()
            accountRepository.verifyEmail(otp.toInt())
            loadingCounter.removeLoader()
            navigate()
        }catch (e:ResponseException){
            loadingCounter.removeLoader()
            if(e.response.status == HttpStatusCode.BadRequest){
                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
            }
            uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
        } catch (e:Exception){
            loadingCounter.removeLoader()
            uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
            Log.d("DEBUG_APP",e.localizedMessage?:"")
        }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}