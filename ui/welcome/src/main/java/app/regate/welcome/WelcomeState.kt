package app.regate.welcome

import app.regate.api.UiMessage
import app.regate.models.Labels

data class WelcomeState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val categories:List<Labels> = emptyList(),
    val selectedCategories:List<Long> = emptyList()
){
    companion object{
        val Empty = WelcomeState()
    }
}
