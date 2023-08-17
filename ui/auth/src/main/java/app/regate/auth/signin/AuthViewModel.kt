package app.regate.auth.signin

import app.regate.common.resources.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.UserDto
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class AuthViewModel (
    googleSignInClient:GoogleSignInClient,
    private val accountRepository: AccountRepository,
//    private val logger: Logger,
    private val preferences: AppPreferences,
    ): ViewModel(){
    private val genero = MutableStateFlow<Genero?>(null)
    private val birtDay = MutableStateFlow<String>("")
    private val loadingState =ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val googleClient = googleSignInClient

    val state:StateFlow<AuthState> = combine(
        loadingState.observable,
        genero,
        birtDay,
        uiMessageManager.message,
    ){loading,genero,birthDay,message->
        AuthState(
            loading = loading,
            genero = genero,
            birthDay = birthDay,
            message = message,
        )
    }.stateIn(
        scope =viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AuthState.Empty
    )

    init {
        viewModelScope.launch {
            loadingState.observable.collect{
                Log.d("API_REQUEST","Error $it")
//                logger.d(it.toString())
            }
        }
        preferences.observeToken().onEach {
            Log.d("API_REQUEST","TOKEN $it")
        }.launchIn(viewModelScope)
//        setGenero(Genero.MALE)
//        getMe()
    }

    @SuppressLint("SuspiciousIndentation")
    fun login(email:String,password:String,redirect:()->Unit,context:Context){
        viewModelScope.launch {
            loadingState.addLoader()
            try{
                accountRepository.login(
                    LoginRequest(
                    email = email, password = password)
                )
                loadingState.removeLoader()
                redirect()
            }catch (e:ResponseException) {
                loadingState.removeLoader()
                if(e.response.status == HttpStatusCode.BadRequest){
                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
                }else{
                    uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
                }
            } catch(e:Exception){
//                logger.d(e)
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
                Log.d("API_REQUEST",e.localizedMessage?:"Error")
            }
        }
    }
    fun setGenero(value: Genero?){
        genero.tryEmit(value)
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>,navigate:()->Unit) {
        viewModelScope.launch {
            try {
                val account = completedTask.result
                account?.let{
                    val profile = if(it.photoUrl?.path == null) null else it.photoUrl.toString()
                    Log.d("DEBUG_APP", profile.toString());
                    val user = UserDto(
                        user_id = 0,
                        profile_photo =profile,
                        estado = 0,
                        nombre = it.givenName?:"",
                        apellido = it.familyName,
                        email = it.email?:"",
                        username = it.email?:"",
                        social_id = it.id?:"",
                        profile_id = 0,
                    )
                    accountRepository.socialLogin(user)
                }
                navigate()
            } catch (e: Exception) {
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"None"))
            }
        }
    }


    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
