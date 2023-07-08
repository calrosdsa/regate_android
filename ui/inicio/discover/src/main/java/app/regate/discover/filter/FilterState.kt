package app.regate.discover.filter

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.models.Labels

@Immutable
data class FilterState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val amenities:List<Labels> = emptyList(),
    val filterData:FilterInstalacionData = FilterInstalacionData()
)