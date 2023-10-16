package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "grupos",
)
data class Grupo(
    @PrimaryKey
    override val id: Long,
    val uuid:String = "",
    val name:String ="",
    val description: String? = null,
    val created_at: Instant? = null,
    val photo:String? = null,
    val is_visible:Boolean = false,
    //id del usuario que creo
    val profile_id:Long = 0,
    val visibility:Int = 1,
    val last_message:String = "",
    val last_message_created: Instant? = null,
    val messages_count:Int = 0,
):AppEntity