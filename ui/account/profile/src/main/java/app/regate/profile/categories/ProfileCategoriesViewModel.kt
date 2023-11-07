package app.regate.profile.categories

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.domain.observers.labels.ObserveLabelType
import app.regate.domain.observers.user.ObserveProfileCategory
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.combine
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileCategoriesViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeLabelType: ObserveLabelType,
    observeProfileCategory: ObserveProfileCategory,
):ViewModel() {
    private val id = savedStateHandle.get<Long>("id")?:0
    private val loadingCouter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val myCategories = MutableStateFlow<List<Labels>>(emptyList())
    val state:StateFlow<ProfileCategoriesState> = combine(
        loadingCouter.observable,
        uiMessageManager.message,
        myCategories,
        observeLabelType.flow,
    ) {loading,message,myCategories,categories->
        ProfileCategoriesState(
            loading = loading,
            message = message,
            userCategories = myCategories,
            categories =categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileCategoriesState.Empty
    )
    init{
        observeLabelType(ObserveLabelType.Params(type = LabelType.CATEGORIES))
        observeProfileCategory(ObserveProfileCategory.Params(id))

        viewModelScope.launch {
            observeProfileCategory.flow.collectLatest {categories->
                try{
                    myCategories.emit(categories)
                }catch(e:Exception){
                    Log.d("DEBUG_APP_",e.localizedMessage?:"")
                }
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun add(id:Long){
        viewModelScope.launch {
            try{
                if(!myCategories.value.map { it.id }.contains(id)){
                val category = state.value.categories.find{ it.id == id }
                if(category != null){
                    val updateList = myCategories.value + category
                    myCategories.emit(updateList)
                }
                }
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    fun remove(id:Long){
        viewModelScope.launch {
            try{
                val updateList = myCategories.value.filter { it.id != id }
                myCategories.emit(updateList)
            }catch (e:Exception){
                //TODO()
            }
        }
    }
}