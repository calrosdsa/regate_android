package app.regate.grupo.invitations

import GrupoInvitationRequest
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.grupo.GrupoRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAddressDevice
import app.regate.domain.observers.grupo.ObservePagerInvitations
import app.regate.domain.pagination.search.PaginationSearchProfiles
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class InviteUserViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository,
    private val grupoRepository: GrupoRepository,
    observeAddressDevice: ObserveAddressDevice,
    pagingSource:ObservePagerInvitations,
    ):ViewModel() {
    private val filterData = MutableStateFlow(FILTER_DATA)
    val pagedList: Flow<PagingData<ProfileDto>> = Pager(PAGING_CONFIG){
        PaginationSearchProfiles(init =filterData.value.query.isNotBlank()){page->
            usersRepository.searchProfiles(filterData.value,page,20)
//            salaRepository.filterSalas()
        }
    }.flow.cachedIn(viewModelScope)
    val pagedListInvitations = pagingSource.flow.cachedIn(viewModelScope)
    private val grupoId = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val selectedIds = MutableStateFlow(HashMap<Long,Int>())
    val state:StateFlow<InviteUserState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeAddressDevice.flow,
        filterData,
        selectedIds,
    ){loading,message,addressDevice,filterData,selectedIds->
        InviteUserState(
            loading = loading,
            message = message,
            addressDevice = addressDevice,
            filterData = filterData,
            selectedIds = selectedIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InviteUserState.Empty
    )

    init {
        observeAddressDevice(Unit)
        pagingSource(ObservePagerInvitations.Params(
            pagingConfig = PAGING_CONFIG,
            grupoId = grupoId
        ))
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
        val FILTER_DATA = SearchFilterRequest()

    }
    fun search(query:String){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
                val formatQuery = query.lowercase().trim().replace(" ",":* | ") + ":*"
                val addressDevice = state.value.addressDevice
                val d = filterData.value.copy(
                    query = formatQuery,
                    longitud = addressDevice?.longitud?:0.0,
                    latitud = addressDevice?.latitud?:0.0,
                )
                filterData.emit(d)
//                delay(1000)
                Log.d("DEBUG_APP_SEARCH",d.query)
                loadingCounter.removeLoader()
            }catch (e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }

    fun sendInvitation(profileId:Long,update:()->Unit){
        viewModelScope.launch {
            try{
                val grupo = grupoRepository.getGrupo(grupoId)
                val request = GrupoInvitationRequest(
                    profile_id = profileId,
                    grupo_id = grupoId,
                    grupo_name = grupo.name,
                    grupo_photo = grupo.photo
                )
                grupoRepository.sendInvitation(request)
                update()
            }catch (e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }
    fun cancelInvitation(profileId:Long,remove:()->Unit){
        viewModelScope.launch {
            try{
                val request = GrupoInvitationRequest(
                    profile_id = profileId,
                    grupo_id = grupoId
                )
                grupoRepository.declineGrupoInvitation(request)
                remove()
            }catch (e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }


}