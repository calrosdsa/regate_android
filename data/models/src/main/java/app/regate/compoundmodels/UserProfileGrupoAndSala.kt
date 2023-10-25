package app.regate.compoundmodels

data class UserProfileGrupoAndSala(
    val profile_id:Long,
    val nombre:String,
    val apellido:String?,
    val profile_photo:String?,
    val is_admin:Boolean,
    val id:Long
)
