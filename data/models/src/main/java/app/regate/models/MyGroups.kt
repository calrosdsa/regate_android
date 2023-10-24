package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado

@Entity(
    tableName = "my_groups",
)
data class MyGroups(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val request_estado:GrupoRequestEstado = GrupoRequestEstado.JOINED,
    ):AppEntity
