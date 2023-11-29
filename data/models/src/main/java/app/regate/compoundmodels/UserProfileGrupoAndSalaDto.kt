package app.regate.compoundmodels

import app.regate.data.dto.chat.TypeChat
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileGrupoAndSalaDto(
    val profile_id:Long,
    val nombre:String,
    val apellido:String? = null,
    val profile_photo:String? = null,
    val is_admin:Boolean = false,
    val is_out:Boolean = false,
    val id:Long,
    val parent_id:Long = 0,
    val type_chat:Int = 1
)
