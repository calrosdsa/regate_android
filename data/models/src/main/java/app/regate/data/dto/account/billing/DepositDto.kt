package app.regate.data.dto.account.billing

import kotlinx.serialization.Serializable

@Serializable
data class DepositDto(
    val amount: Double,
    val created_at: String,
    val gloss: String,
    val id: Long,
    val profile_id: Long,
    val transaction_datetime: String
)


@Serializable
data class DepositPaginationResponse(
    val results:List<DepositDto>,
    val nextPage:Int,
)
