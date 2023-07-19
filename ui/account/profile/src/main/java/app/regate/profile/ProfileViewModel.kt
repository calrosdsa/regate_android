package app.regate.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveProfile
import app.regate.domain.observers.ObserveUser
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository,
    observeProfile: ObserveProfile,
    observeUser: ObserveUser,
):ViewModel() {
    private val id = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state:StateFlow<ProfileState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeProfile.flow,
        observeUser.flow
    ){loading,message,profile,user->
        ProfileState(
            loading = loading,
            message = message,
            profile = profile,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileState.Empty
    )
    init{
        observeProfile(ObserveProfile.Params(id))
        observeUser(Unit)
        getProfile()
    }

    fun getProfile(){
        viewModelScope.launch {
            try{
                usersRepository.getProfile(id)
            }catch (e:Exception){
                //TODO()
            }
        }
    }
}