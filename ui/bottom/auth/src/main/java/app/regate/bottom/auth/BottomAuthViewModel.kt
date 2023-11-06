package app.regate.bottom.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.auth.UserDto
import app.regate.util.ObservableLoadingCounter
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class BottomAuthViewModel(
    googleSignInClient: GoogleSignInClient,
    private val accountRepository: AccountRepository
    ):ViewModel(){
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val googleClient = googleSignInClient
    val state: StateFlow<AuthBottomState> = combine(
        loadingState.observable,
        uiMessageManager.message,
    ){loading,message ->
        AuthBottomState(
            loading = loading,
            message = message,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AuthBottomState.Empty
    )

    init {
    }



    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>,navigate:()->Unit) {
        viewModelScope.launch {
            try {
                val account = completedTask.result
                account?.let{
                    val profile = if(it.photoUrl?.path == null) null else it.photoUrl.toString()
                    Log.d("DEBUG_APP", profile.toString());
                    val user = UserDto(
                        profile_photo =profile,
                        nombre = it.givenName?:"",
                        apellido = it.familyName,
                        email = it.email?:"",
                        username = it.email?:"",
                        social_id = it.id?:"",
                )
                    accountRepository.socialLogin(user)
                }
                navigate()
            } catch (e: Exception) {
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"None"))
            }
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}