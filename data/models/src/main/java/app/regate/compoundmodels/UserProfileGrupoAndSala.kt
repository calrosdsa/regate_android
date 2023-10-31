package app.regate.compoundmodels

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileGrupoAndSala(
    val profile_id:Long,
    val nombre:String,
    val apellido:String?,
    val profile_photo:String?,
    val is_admin:Boolean,
    val is_out:Boolean,
    val id:Long
)
