package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="establecimientos",
)
data class Establecimiento(
    @PrimaryKey
    override val id: Long,
    val address: String?,
    val created_at: String?,
    val email: String?,
    val empresa_id: Int?,
    val latidud: String?,
    val longitud: String?,
    val name: String,
    val description:String?  =null,
    val is_open:Boolean? = null,
    val phone_number: String?,
    val photo: String?,
    val portada: String?,
    val amenities:List<Long> = emptyList(),
    val rules:List<Long> = emptyList(),
):AppEntity
