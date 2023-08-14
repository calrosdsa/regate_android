package app.regate.data.dto.account.billing

import kotlinx.serialization.Serializable

@Serializable
data class ConsumeDto(
    val amount: Double,
    val created_at: String,
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
