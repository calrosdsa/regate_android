package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.establecimiento.Establecimiento
import app.regate.models.Instalacion
import app.regate.models.Reserva
import java.util.Objects

class ReservaDetail {
    @Embedded
    lateinit var reserva:Reserva
    @Relation(parentColumn = "establecimiento_id", entityColumn = "id")
    var establecimiento: Establecimiento? = null
    @Relation(parentColumn = "instalacion_id", entityColumn = "id")
    var instalacion: Instalacion? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ReservaDetail
        if (reserva != other.reserva) return false
        if (establecimiento != other.establecimiento) return false
        if (instalacion != other.instalacion) return false
        return true
    }

    //    override fun hashCode(): Int {
//        var result = message.hashCode()
//        result = 31 * result + profile.hashCode()
//        return result
//    }
    override fun hashCode(): Int = Objects.hash(reserva,establecimiento,instalacion)
}