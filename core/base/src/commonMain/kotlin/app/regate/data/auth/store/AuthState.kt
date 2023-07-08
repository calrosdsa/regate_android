package app.regate.data.auth.store

interface AuthState {
    val accessToken: String
    val refreshToken: String
    val isAuthorized: Boolean
    fun serializeToJson(): String

    companion object {
        val Empty: AuthState = object : AuthState {
            override val accessToken: String = ""
            override val refreshToken: String = ""
            override val isAuthorized: Boolean = false
            override fun serializeToJson(): String = "{}"
        }
    }
}
