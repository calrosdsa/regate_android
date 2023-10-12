package app.regate.media.photo

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoState(
    val images:List<String> = emptyList(),
    val selectedIndex:Int? = null
){
    companion object {
        val Empty = PhotoState()
    }
}
