package app.regate.media.photo

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoState(
    val images:List<String> = emptyList()
){
    companion object {
        val Empty = PhotoState()
    }
}
