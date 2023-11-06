package app.regate.models.grupo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import app.regate.models.Establecimiento
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(

    tableName = "invitation_grupo",
    indices = [
        Index(value = ["grupo_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Grupo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("grupo_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["profile_id", "grupo_id"]
)
data class InvitationGrupo(
    val profile_id:Long,
    val grupo_id:Long,
    val estado:Int,
    val created_at:Instant = Clock.System.now(),
)
