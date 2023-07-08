package app.regate.map.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ActiviViewModel(@Assisted handle:SavedStateHandle):ViewModel() {
    private val episodeId: Int = handle["id"]!!

    init{
        Log.d("DEBUG_APP",episodeId.toString())
    }


    fun showMessage(){
        Log.d("DEBUG_APP_e",episodeId.toString())
    }
}