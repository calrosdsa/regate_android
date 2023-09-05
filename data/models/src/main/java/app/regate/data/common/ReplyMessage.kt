package app.regate.data.common

import app.regate.data.dto.empresa.grupo.GrupoMessageType

data class ReplyMessageData(
    val nombre:String? = null,
    val apellido:String? = null,
    val content:String,
    val type_message:Int = GrupoMessageType.MESSAGE.ordinal,
    val id:Long,
    val data:String? = null,
)
