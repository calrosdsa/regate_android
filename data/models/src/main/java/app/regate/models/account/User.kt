package app.regate.models.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.models.AppEntity

@Entity(
    tableName ="users"
)
data class User(
    @PrimaryKey
    override val id: Long = 0,
    val user_id: Long,
    val email: String,
    val estado: Int,
    val username: String,
//    val profile_photo:String?=null,
//    val nombre:String,
//    val apellido:String? = null,
    val profile_id:Long
): AppEntity
