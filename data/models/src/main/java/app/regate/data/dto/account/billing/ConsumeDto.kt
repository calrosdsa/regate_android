package app.regate.data.dto.account.billing

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ConsumeDto(
    val amount: Double,
    val created_at: Instant,
    val id: Long,
    val message:String,
    val id_entity:Long,
    val type_entity:Int,
    val profile_id: Long,
)


@Serializable
data class ConsumePaginationResponse(
    val results:List<ConsumeDto>,
    val nextPage:Int,
)

enum class TypeEntity {
    NONE,
    ENTITY_SALA,
    ENTITY_GROUP,
    ENTITY_ACCOUNT,
    ENTITY_BILLING,
    ENTITY_RESERVA,
}