package app.regate.data.dto.empresa.coin

import kotlinx.serialization.Serializable

@Serializable
data class TokenQrReponse(
    val success:Boolean,
    val message:String
)
