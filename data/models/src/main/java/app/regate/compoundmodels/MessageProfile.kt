package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import app.regate.models.Message
import app.regate.models.Profile
import java.util.Objects

class MessageProfile {
    @Embedded
    lateinit var message:Message
    @Relation(parentColumn = "profile_id", entityColumn = "id")
    var profile:Profile? = null
    @Relation(parentColumn = "reply_to", entityColumn = "id")
    var reply:Message? = null

//    @delegate:Ignore
//    val indexItem by lazy { this.hashCode() }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MessageProfile
        if (message != other.message) return false
        if (profile != other.profile) return false
        if (reply != other.reply) return false
        return true
    }

//    override fun hashCode(): Int {
//        var result = message.hashCode()
//        result = 31 * result + profile.hashCode()
//        return result
//    }
    override fun hashCode(): Int = Objects.hash(message,profile,reply)
}