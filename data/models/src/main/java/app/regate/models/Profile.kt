package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName ="profiles",
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("user_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class Profile(
    @PrimaryKey
    override val id: Long = 0,
    val uuid:String = "",
    val user_id: Long? = null,
    val email: String? = null,
    val profile_photo:String?=null,
    val nombre:String,
    val apellido:String? = null,
    val created_at:Instant? = null
):AppEntity

