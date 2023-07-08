package app.regate.settings
import androidx.core.content.edit
import me.tatarka.inject.annotations.Inject

import android.content.SharedPreferences
import app.regate.data.auth.AppAuthAuthStateWrapper
import app.regate.data.auth.store.AuthState
import app.regate.data.auth.store.AuthStore

typealias AuthSharedPreferences = SharedPreferences


@Inject
class PreferencesAuthStore(
    private val authPrefs: Lazy<AuthSharedPreferences>,
) : AuthStore {
    override suspend fun get(): AuthState? {
        return authPrefs.value
            .getString(PreferenceAuthKey, null)
            ?.let(::AppAuthAuthStateWrapper)
    }


    override suspend fun save(state: AuthState) {
        authPrefs.value.edit(commit = true) {
            putString(PreferenceAuthKey, state.serializeToJson())
            commit()
        }
    }

    override suspend fun clear() {
        authPrefs.value.edit(commit = true) {
            remove(PreferenceAuthKey)
        }
    }

    private companion object {
        private const val PreferenceAuthKey = "stateJson"
    }
}