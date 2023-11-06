package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.Profile
import app.regate.models.ProfileCategory
import app.regate.models.account.User

class ProfileDetail {
    @Embedded
    lateinit var profile: Profile
    @Relation(parentColumn = "profile_id", entityColumn = "id")
    var categories: List<ProfileCategory> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ProfileDetail
        if (profile != other.profile) return false
        if (categories != other.categories) return false
        return true
    }

    override fun hashCode(): Int {
        var result = profile.hashCode()
        result = 31 * result + categories.hashCode()
        return result
    }
}