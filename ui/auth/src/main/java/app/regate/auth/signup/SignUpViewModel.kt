package app.regate.auth.signup

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.auth.SignUpRequest
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.ResponseMessage
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

@Inject
class SignUpViewModel (
    private val accountRepository: AccountRepository
        ): ViewModel(){
    private val uiMessageManager =UiMessageManager()
    private val loadingCounter = ObservableLoadingCounter()
    private val genero = MutableStateFlow<Genero?>(null)
    private val birtDay = MutableStateFlow("")

    val state:StateFlow<SignUpState> = combine(
        genero,
        birtDay,
        loadingCounter.observable,
        uiMessageManager.message,
    ){genero,birthDay,loading,message->
        SignUpState(
            genero = genero,
            birthDay = birthDay,
            loading = loading,
            message = message
        )
    }.stateIn(
        scope =viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SignUpState.Empty
    )

    init {
//        setGenero(Genero.MALE)
    }

    fun signUp(username:String,email:String,password:String,context:Context,navigate:()->Unit){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
                val request = SignUpRequest(
                    email = email,
                    password = password,
                    username = username
                )
                accountRepository.signUp(request)
                loadingCounter.removeLoader()
                navigate()
            } catch (e:ResponseException){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
                loadingCounter.removeLoader()
                if(e.response.status == HttpStatusCode.BadRequest){
                    uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
                }
            } catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
            }
        }
    }

    fun setGenero(value: Genero?){
        genero.tryEmit(value)
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}