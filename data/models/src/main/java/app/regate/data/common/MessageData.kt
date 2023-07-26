package app.regate.data.common

data class MessageData(
    val content:String,
    val reply_to:Long? = null
)