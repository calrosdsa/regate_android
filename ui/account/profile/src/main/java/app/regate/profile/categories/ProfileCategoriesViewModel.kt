package app.regate.profile.categories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.account.user.ProfileCategoryRequest
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.labels.ObserveLabelType
import app.regate.domain.observers.user.ObserveProfileCategory
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.combine
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay
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
    private val usersRepository: UsersRepository
):ViewModel() {
    private val id = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val myCategories = MutableStateFlow<List<Labels>>(emptyList())
    private val requestData = MutableStateFlow<List<ProfileCategoryRequest>>(emptyList())
    val state:StateFlow<ProfileCategoriesState> = combine(
        loadingCounter.observable,
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
                    val data = categories.map{categorie-> ProfileCategoryRequest(
                        category_id = categorie.id.toInt(),
                        should_delete = false,
                        should_insert = false
                    ) }
                    requestData.emit(data)
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

    fun save(){
        viewModelScope.launch {
            try{

                loadingCounter.addLoader()
                delay(2000)
                Log.d("DEBUG_APP_",requestData.value.toList().toString())
                usersRepository.updateCategoriesProfile(requestData.value.toList(),id)
                uiMessageManager.emitMessage(UiMessage(message = "Se han aplicado los cambios"))
                loadingCounter.removeLoader()
            }catch (e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
            }
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

                val list = mutableListOf<ProfileCategoryRequest>()
                if(requestData.value.map { it.category_id }.contains(id.toInt())){
                    //TODO()
//                    requestData.value.map {result->
//                        if(result.category_id == id){
//                            if(!result.should_insert){
//                            list.add(result.copy(should_delete = true))
//                            }else{
//                                //TODO()
//                            }
//                        }
//                        list.add(result)
//                    }
//                    requestData.emit(list.toList())
                }else{
                    Log.d("DEBUG_APP_","ADD VAL")
                    list.addAll(requestData.value)
                    list.add(
                        ProfileCategoryRequest(
                        category_id = id.toInt(),
                        should_delete = false,
                        should_insert = true
                    )
                    )
                    requestData.emit(list.toList())
                }
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun remove(id:Long){
        viewModelScope.launch {
            try{
                val updateList = myCategories.value.filter { it.id != id }
                myCategories.emit(updateList)
                val list = mutableListOf<ProfileCategoryRequest>()
                    requestData.value.map {result->
                        if(result.category_id == id.toInt()){
                            if(!result.should_insert){
                            list.add(result.copy(should_delete = true))
                            } else {
                                //TODO()
                            }
                        }else{
                        list.add(result)
                        }
                    }
                requestData.emit(list.toList())
            }catch (e:Exception){
                //TODO()
            }
        }
    }
}