package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_grupo",
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
)
data class UserGrupo(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val profile_id:Long,
    val grupo_id:Long,
    val is_admin:Boolean,
    val is_out:Boolean = false
):AppEntity
