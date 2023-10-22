package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import kotlinx.datetime.Instant

@Entity(
    tableName = "my_groups",
)
data class MyGroups(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val group_id:Long =0,
    val request_estado:GrupoRequestEstado = GrupoRequestEstado.JOINED,
    val last_message:String = "",
    val last_message_created: Instant? = null,
    val messages_count:Int = 0,
    ):AppEntity
