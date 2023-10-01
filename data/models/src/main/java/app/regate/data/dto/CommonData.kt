package app.regate.data.dto

import kotlinx.serialization.Serializable

data class FileData(
    val name:String,
    val type:String,
    val byteArray: ByteArray?= null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileData

        if (name != other.name) return false
        if (type != other.type) return false
        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        return result
    }
}

