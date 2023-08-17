package app.regate.complejo.reviews

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState

data class ReviewsState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
) {
    companion object {
        val Empty = ReviewsState()
    }
}