package app.regate.compoundmodels

import kotlinx.datetime.Instant


data class ChatWithMessage(
    val id: Long = 0,
    val photo:String? = null,
    val name:String = "",
    val last_message:String? = null,
    val last_message_created: Instant? = null,
    val messages_count:Int = 0
)
