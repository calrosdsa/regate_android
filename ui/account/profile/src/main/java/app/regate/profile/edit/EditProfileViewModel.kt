package app.regate.profile.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.FileData
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.user.ObserveProfile
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class EditProfileViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeProfile: ObserveProfile,
    private val usersRepository: UsersRepository
):ViewModel(){
    private val id = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val file = MutableStateFlow<FileData?>(null)

    val state:StateFlow<EditProfileState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeProfile.flow
    ){loading,message,profile->
        EditProfileState(
            loading = loading,
            message = message,
            profile = profile
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EditProfileState.Empty
    )

    init{
        observeProfile(ObserveProfile.Params(id = id))
    }
    fun editProfile(nombre:String,apellido:String){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
            state.value.profile?.let {
                usersRepository.editProfile(
                    file = file.value,
                    d = it.copy(
                        apellido = apellido,
                        nombre = nombre,
                    )
                )
            }
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = "Se han aplicado los cambios"))
            } catch (e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
            }
        }
    }
    fun uploadImage(type:String,name:String,byteArray: ByteArray){
        viewModelScope.launch {
            val result = FileData( name = name,type = type, byteArray = byteArray)
            file.tryEmit(result)
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}