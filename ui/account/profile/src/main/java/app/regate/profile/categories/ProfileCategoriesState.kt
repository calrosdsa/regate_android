package app.regate.profile.categories

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.models.Labels

@Immutable
data class ProfileCategoriesState(
    val loading:Boolean = false,
    val message: UiMessage? = null,
    val categories:List<Labels> = emptyList(),
    val userCategories:List<Labels> = emptyList(),
){
    companion object{
        val Empty = ProfileCategoriesState()
    }
}
