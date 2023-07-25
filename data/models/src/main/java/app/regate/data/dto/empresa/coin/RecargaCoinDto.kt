package app.regate.data.dto.empresa.coin

import kotlinx.serialization.Serializable

@Serializable
data class RecargaCoinDto(
    val amount: Int,
    val id: Int,
    val price: Double
)