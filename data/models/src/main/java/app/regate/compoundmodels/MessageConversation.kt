package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.Message
import app.regate.models.MessageInbox
import java.util.Objects


class MessageConversation {
    @Embedded
    lateinit var message:MessageInbox
    @Relation(parentColumn = "reply_to", entityColumn = "id")
    var reply: MessageInbox? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MessageConversation
        if (message != other.message) return false
        if (reply != other.reply) return false
        return true
    }

    //    override fun hashCode(): Int {
//        var result = message.hashCode()
//        result = 31 * result + profile.hashCode()
//        return result
//    }
    override fun hashCode(): Int = Objects.hash(message,reply)
}