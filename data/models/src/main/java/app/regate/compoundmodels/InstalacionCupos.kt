package app.regate.compoundmodels

import androidx.room.Embedded
import androidx.room.Relation
import app.regate.models.Cupo
import app.regate.models.Instalacion

class InstalacionCupos {
    @Embedded
    lateinit var instalacion: Instalacion
    @Relation(parentColumn = "id", entityColumn = "instalacion_id")
    lateinit var cupos:List<Cupo>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InstalacionCupos
        if (instalacion != other.instalacion) return false
        if (cupos != other.cupos) return false
        return true
    }

    override fun hashCode(): Int {
        var result = instalacion.hashCode()
        result = 31 * result + cupos.hashCode()
        return result
    }
}