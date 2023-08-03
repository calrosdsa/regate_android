package app.regate.data.dto.empresa.coin

import kotlinx.serialization.Serializable

@Serializable
data class QrResponse(
    val id:String,
    val qr:String
)
