package app.regate.auth.signup.emailverification

import app.regate.api.UiMessage
import app.regate.models.account.User

data class EmailVerificationState(
    val loading:Boolean = false,
    val message: UiMessage?  = null,
    val user: User? = null
){
    companion object{
        val Empty = EmailVerificationState()
    }
}
