package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable

@Serializable
data class SalaFilterData(
    val categories:List<Long> = emptyList(),
    val isInit:Boolean = false
)
