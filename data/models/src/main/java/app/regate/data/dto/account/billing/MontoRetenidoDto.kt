package app.regate.data.dto.account.billing

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MontoRetenidoDto(
    val parent_id:Int,
    val message:String,
    val type_entity:Int,
    val amount:Double,
    val created_at:Instant,
)

@Serializable
data class MontoRetenidoPaginationRespone(
    val results:List<MontoRetenidoDto> = emptyList(),
    val nextPage:Int
)