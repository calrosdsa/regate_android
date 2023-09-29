package app.regate.media.photo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.app.MediaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PhotoViewModel(
    @Assisted savedStateHandle: SavedStateHandle
) :ViewModel(){
    private val data = savedStateHandle.get<String>("data")
    private val images = MutableStateFlow<List<String>>(emptyList())
    val state:StateFlow<PhotoState> = combine(
        images
    ){images->
        PhotoState(
            images = images[0]
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PhotoState.Empty
    )

    init {
        getImages()
    }

    private fun getImages(){
        Log.d("DEBUG_APP_IMAGES",data.toString())
        if(data == null) return
        viewModelScope.launch {
        try{
            val data = Json.decodeFromString<MediaData>(data)
            images.tryEmit(data.images)
        }catch (err:Exception){
            //TODO()
        }
        }
    }
}