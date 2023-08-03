package app.regate.data.dto.empresa.coin

import kotlinx.serialization.Serializable

@Serializable
data class QrRequest(
    val additionalData: String,
    val amount: Int,
    val currency: String,
    val destinationAccountId: String,
    val expirationDate: String,
    val gloss: String,
    val singleUse: Boolean
)