package app.regate.sala.complete

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.dto.empresa.salas.SalaCompleteDto
import app.regate.models.User

data class SalaCompleteState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val salaCompleteDetail:SalaCompleteDetail? = null,
    val user:User? = null
){
    companion object{
        val Empty = SalaCompleteState()
    }
}
