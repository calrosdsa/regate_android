package app.regate.filterSalas

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaFilterData

data class FilterSalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val authState:AppAuthState = AppAuthState.LOGGED_OUT,
    val filterData: SalaFilterData = SalaFilterData()
){
    companion object{
         val Empty = FilterSalaState()
    }
}
