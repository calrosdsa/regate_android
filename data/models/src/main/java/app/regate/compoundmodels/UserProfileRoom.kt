package app.regate.compoundmodels

data class UserProfileRoom(
    val id:Long,
    val nombre:String,
    val apellido:String?,
    val profile_photo:String?,
    val is_admin:Boolean,
    val user_sala_id:Long
)
