package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.user.Profile
import app.regate.models.account.User

class UserProfile {
    @Embedded
    lateinit var user: User
    @Relation(parentColumn = "profile_id", entityColumn = "id")
    var profile: Profile?= null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserProfile
        if (user != other.user) return false
        if (profile != other.profile) return false
        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + profile.hashCode()
        return result
    }
}