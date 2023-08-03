package app.regate.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.domain.observers.ObserveLabelType
import app.regate.models.LabelType
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class WelcomeViewmModel(
    observeLabelType: ObserveLabelType,
    private val appPreferences: AppPreferences
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val selectedCategories = MutableStateFlow<List<Long>>(emptyList())
    val state:StateFlow<WelcomeState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeLabelType.flow,
        selectedCategories,
    ){loading,message,categories,selectedCategories ->
        WelcomeState(
            loading = loading,
            message = message,
            categories = categories,
            selectedCategories = selectedCategories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = WelcomeState.Empty
    )

    init{
        observeLabelType(ObserveLabelType.Params(LabelType.CATEGORIES))
    }

    fun addCategory(id:Long){
        viewModelScope.launch {
            if(selectedCategories.value.contains(id)){
                val mutableList = selectedCategories.value.toMutableList()
                mutableList.remove(id)
                selectedCategories.tryEmit(mutableList.toList())
            }else{
            selectedCategories.tryEmit(selectedCategories.value.plus(id))
            }
        }
    }

    fun saveCategories(){
        try{
            appPreferences.categories = Json.encodeToString(selectedCategories.value)
        }catch(e:Exception){
            Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
        }
    }
}