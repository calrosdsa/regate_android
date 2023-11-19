package app.regate.compoundmodels.grupo

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.user.Profile
import app.regate.models.grupo.InvitationGrupo

class UserInvitationGrupo {
    @Embedded
    lateinit var invitation: InvitationGrupo
    @Relation(parentColumn = "profile_id", entityColumn = "id")
    var profile: Profile?= null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserInvitationGrupo
        if (invitation != other.invitation) return false
        if (profile != other.profile) return false
        return true
    }

    override fun hashCode(): Int {
        var result = invitation.hashCode()
        result = 31 * result + profile.hashCode()
        return result
    }
}
