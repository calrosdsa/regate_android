package app.regate.compoundmodels.grupo

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.Profile
import app.regate.models.grupo.Grupo
import app.regate.models.grupo.InvitationGrupo

class UserInvitation {
    @Embedded
    lateinit var invitation: InvitationGrupo
    @Relation(parentColumn = "grupo_id", entityColumn = "id")
    var grupo: Grupo?= null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserInvitation
        if (invitation != other.invitation) return false
        if (grupo != other.grupo) return false
        return true
    }

    override fun hashCode(): Int {
        var result = invitation.hashCode()
        result = 31 * result + grupo.hashCode()
        return result
    }
}
