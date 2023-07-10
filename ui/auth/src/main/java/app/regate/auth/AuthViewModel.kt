package app.regate.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
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
    val s = ""

    val state:StateFlow<AuthState> = combine(
        loadingState.observable,
        genero,
        birtDay
    ){loading,genero,birthDay->
        AuthState(
            loading = loading,
            genero = genero,
            birthDay = birthDay
        )
    }.stateIn(
        scope =viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue =AuthState.Empty
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
    fun login(email:String,password:String,redirect:()->Unit){
        viewModelScope.launch {
            loadingState.addLoader()
            try{
                delay(2000)
                accountRepository.login(
                    LoginRequest(
                    email = email, password = password)
                )
                loadingState.removeLoader()
                redirect()
//                redirect()
            }catch(e:Exception){
//                logger.d(e)
                loadingState.removeLoader()
                Log.d("API_REQUEST",e.localizedMessage?:"Error")
            }
        }
    }
    fun setGenero(value:Genero?){
        genero.tryEmit(value)
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>,navigate:()->Unit) {
        Log.d("AUTH-SUCCESS","EXECUTTINH");
        viewModelScope.launch {

        try {
//            val account = completedTask.result
            val account = completedTask.result
            Log.d("AUTH-SUCCESS", account.displayName.toString());
            Log.d("AUTH-SUCCESS", account.email.toString());
            Log.d("AUTH-SUCCESS", account.photoUrl.toString());
            Log.d("AUTH-SUCCESS", account.id.toString());

//            delay(500)
            navigate()
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (e: Exception) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("AUTH-ERROR", "signInResult:failed code22=" + e.localizedMessage);
//            updateUI(null);
        }
    }
    }
}
