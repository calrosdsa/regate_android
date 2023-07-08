package app.regate.data.auth

import app.regate.data.auth.store.AuthState


class AppAuthAuthStateWrapper(
//    val authState: net.openid.appauth.AuthState,
    private val token:String
) : AuthState {
//    constructor(json: String) : this(net.openid.appauth.AuthState.jsonDeserialize(json))

    override val accessToken: String get() = token
    override val refreshToken: String get() = token
    override val isAuthorized: Boolean get() = true
    override fun serializeToJson(): String = token
}
