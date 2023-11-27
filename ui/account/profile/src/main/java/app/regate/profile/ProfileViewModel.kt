package app.regate.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.account.user.EstablecimientoItemDto
import app.regate.data.dto.system.ReportData
import app.regate.data.dto.system.ReportType
import app.regate.data.grupo.GrupoRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.user.ObserveProfile
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObserveMyGroups
import app.regate.domain.observers.grupo.ObserveUserGroups
import app.regate.domain.observers.user.ObserveProfileCategory
import app.regate.extensions.combine
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository,
    private val grupoRepository: GrupoRepository,
    observeProfile: ObserveProfile,
    observeUser: ObserveUser,
    observeProfileCategory:ObserveProfileCategory,
    observeUserGroups: ObserveUserGroups,
):ViewModel() {
    private val id = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val establecimientos= MutableStateFlow<List<EstablecimientoItemDto>>(emptyList())
    val state:StateFlow<ProfileState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeProfile.flow,
        observeUser.flow,
        observeProfileCategory.flow,
        observeUserGroups.flow,
        establecimientos,
    ){loading,message,profile,user,categories,grupos,establecimientos->
        ProfileState(
            loading = loading,
            message = message,
            profile = profile,
            user = user,
            categories = categories,
            grupos = grupos,
            establecimientos = establecimientos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileState.Empty
    )
    init{
        observeProfile(ObserveProfile.Params(id))
        observeProfileCategory(ObserveProfileCategory.Params(id))
        observeUserGroups(Unit)
        observeUser(Unit)
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val res = usersRepository.getProfile(id)
                establecimientos.emit(res)
                grupoRepository.myGroups()
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    fun navigateToReport(navigate:(String)->Unit){
        try{
            val report = Json.encodeToString(ReportData(
                report_type = ReportType.PROFILE.ordinal,
                entity_id = id.toInt(),
            ))
            navigate(report)
        }catch (e:Exception){
            //TODO()
        }
    }
}