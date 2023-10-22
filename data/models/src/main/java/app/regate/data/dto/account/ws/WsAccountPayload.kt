package app.regate.data.dto.account.ws

import kotlinx.serialization.Serializable

@Serializable
data class WsAccountPayload(
    val type:Int,
    val payload:String
)
