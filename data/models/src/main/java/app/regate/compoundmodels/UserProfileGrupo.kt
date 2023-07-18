package app.regate.compoundmodels

data class UserProfileGrupo(
    val id:Long,
    val nombre:String,
    val apellido:String?,
    val profile_photo:String?,
    val is_admin:Boolean,
    val user_group_id:Long
)
