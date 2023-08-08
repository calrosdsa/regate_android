package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="establecimientos",
)
data class Establecimiento(
    @PrimaryKey
    override val id: Long,
    val address: String? = null,
    val created_at: String? = null,
    val email: String? =null,
    val empresa_id: Int? = null,
    val latidud: String? = null,
    val longitud: String? = null,
    val name: String,
    val description:String?  =null,
    val is_open:Boolean? = null,
    val phone_number: String? = null,
    val photo: String? = null,
    val address_photo:String? = null,
    val amenities:List<Long> = emptyList(),
    val rules:List<Long> = emptyList(),
):AppEntity
