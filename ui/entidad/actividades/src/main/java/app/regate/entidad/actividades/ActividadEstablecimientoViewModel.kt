package app.regate.entidad.actividades

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ActividadEstablecimientoViewModel(
//    handle: SavedStateHandle,
):ViewModel() {
//    private val establecimientoId: Int = handle["id"]!!
     private val establecimientoId: Int = 1

    init{
        Log.d("DEBUG_APP22",establecimientoId.toString())
    }

    fun hello(){
        Log.d("DEBUG_APP22","121")

    }

}