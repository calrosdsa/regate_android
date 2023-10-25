package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.Message
import app.regate.models.Profile
import app.regate.models.chat.Chat

class MessageWithChat {
    @Embedded
    lateinit var message: Message
    @Relation(parentColumn = "chat_id", entityColumn = "id")
    var chat: Chat?= null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MessageWithChat
        if (message != other.message) return false
        if (chat != other.chat) return false
        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + chat.hashCode()
        return result
    }
}