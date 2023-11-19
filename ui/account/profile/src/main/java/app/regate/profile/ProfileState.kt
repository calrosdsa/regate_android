package app.regate.profile

import app.regate.api.UiMessage
import app.regate.data.dto.account.user.EstablecimientoItemDto
import app.regate.models.Labels
import app.regate.models.user.Profile
import app.regate.models.account.User
import app.regate.models.grupo.Grupo

data class ProfileState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val profile: Profile? = null,
    val user: User? = null,
    val categories:List<Labels> = emptyList(),
    val grupos:List<Grupo> = emptyList(),
    val establecimientos:List<EstablecimientoItemDto> = emptyList()
){
    companion object{
        val Empty = ProfileState()
    }
}