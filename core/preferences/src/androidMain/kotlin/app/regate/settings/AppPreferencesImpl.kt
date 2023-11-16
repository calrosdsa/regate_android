/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.regate.settings

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.edit
import app.regate.constant.Route
import app.regate.core.preferences.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class AppPreferencesImpl(
    private val context: Application,
    private val sharedPreferences: AppSharedPreferences,
) : AppPreferences {
    private val defaultThemeValue = context.getString(R.string.pref_theme_default_value)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    private val defaultUseDynamicColors = Build.VERSION.SDK_INT >= 31

    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key != null) {
            preferenceKeyChangedFlow.tryEmit(key)
        }
    }
    companion object {
        const val KEY_START_ROUTE = "start_route"
        const val KEY_KEYBOARD_HEIGHT = "keyboard_height"
        const val KEY_CATEGORIES = "categories"
        const val KEY_FILTER = "filter"
        const val KEY_ADDRESS = "address"
        const val KEY_FCM_TOKEN = "pref_fcm_token"
        const val KEY_TOKEN = "pref_token"
        const val KEY_THEME = "pref_theme"
        const val KEY_USE_DYNAMIC_COLORS = "pref_dynamic_colors"
        const val KEY_DATA_SAVER = "pref_data_saver"
        const val KEY_LIBRARY_FOLLOWED_ACTIVE = "pref_library_followed_active"
        const val KEY_LIBRARY_WATCHED_ACTIVE = "pref_library_watched_active"
        const val KEY_UPNEXT_FOLLOWED_ONLY = "pref_upnext_followedonly_active"
    }

    override fun setup() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
    override var startRoute: String
        get() = sharedPreferences.getString(KEY_START_ROUTE, Route.HOME)!!
        set(value) = sharedPreferences.edit{
            putString(KEY_START_ROUTE,value)
        }
    override var keyBoardHeight: Int
        get() = sharedPreferences.getInt(KEY_KEYBOARD_HEIGHT, 750)
        set(value) = sharedPreferences.edit{
            putInt(KEY_KEYBOARD_HEIGHT,value)
        }
    override var categories: String
        get() = sharedPreferences.getString(KEY_CATEGORIES, "")!!
        set(value) = sharedPreferences.edit{
            putString(KEY_CATEGORIES,value)
        }
    override var filter: String
        get() = sharedPreferences.getString(KEY_FILTER, "")!!
        set(value) = sharedPreferences.edit{
            putString(KEY_FILTER,value)
        }
    override fun observeFilter(): Flow<String>  = createPreferenceFlow(KEY_FILTER){ filter }
    override var address: String
        get() = sharedPreferences.getString(KEY_ADDRESS, "")!!
        set(value) = sharedPreferences.edit{
            putString(KEY_ADDRESS,value)
        }
    override fun observeAddress(): Flow<String>  = createPreferenceFlow(KEY_ADDRESS){ address}

    override var fcmToken: String
        get() = sharedPreferences.getString(KEY_FCM_TOKEN, "")!!
        set(value) = sharedPreferences.edit{
            putString(KEY_FCM_TOKEN,value)
        }
    override fun observeFcmToken(): Flow<String>  = createPreferenceFlow(KEY_FCM_TOKEN){ fcmToken }
    override var token: String
        get() = sharedPreferences.getString(KEY_TOKEN, "none")!!
        set(value) = sharedPreferences.edit{
            putString(KEY_TOKEN,value)
        }
    override fun observeToken(): Flow<String>  = createPreferenceFlow(KEY_TOKEN){ token }

    override var theme: AppPreferences.Theme
        get() = getThemeForStorageValue(sharedPreferences.getString(KEY_THEME, defaultThemeValue)!!)
        set(value) = sharedPreferences.edit {
            putString(KEY_THEME, value.storageKey)
        }

    override fun observeTheme(): Flow<AppPreferences.Theme> = createPreferenceFlow(KEY_THEME) { theme }

    override var useDynamicColors: Boolean
        get() = sharedPreferences.getBoolean(KEY_USE_DYNAMIC_COLORS, defaultUseDynamicColors)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_USE_DYNAMIC_COLORS, value)
        }

    override fun observeUseDynamicColors(): Flow<Boolean> {
        return createPreferenceFlow(KEY_USE_DYNAMIC_COLORS) { useDynamicColors }
    }

    override var useLessData: Boolean
        get() = sharedPreferences.getBoolean(KEY_DATA_SAVER, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_DATA_SAVER, value)
        }

    override fun observeUseLessData(): Flow<Boolean> = createPreferenceFlow(KEY_DATA_SAVER) {
        useLessData
    }

    override var libraryFollowedActive: Boolean
        get() = sharedPreferences.getBoolean(KEY_LIBRARY_FOLLOWED_ACTIVE, true)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_LIBRARY_FOLLOWED_ACTIVE, value)
        }

    override fun observeLibraryFollowedActive(): Flow<Boolean> {
        return createPreferenceFlow(KEY_LIBRARY_FOLLOWED_ACTIVE) {
            libraryFollowedActive
        }
    }

    override var libraryWatchedActive: Boolean
        get() = sharedPreferences.getBoolean(KEY_LIBRARY_WATCHED_ACTIVE, true)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_LIBRARY_WATCHED_ACTIVE, value)
        }

    override fun observeLibraryWatchedActive(): Flow<Boolean> {
        return createPreferenceFlow(KEY_LIBRARY_WATCHED_ACTIVE) {
            libraryWatchedActive
        }
    }

    override var upNextFollowedOnly: Boolean
        get() = sharedPreferences.getBoolean(KEY_UPNEXT_FOLLOWED_ONLY, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_UPNEXT_FOLLOWED_ONLY, value)
        }

    override fun observeUpNextFollowedOnly(): Flow<Boolean> {
        return createPreferenceFlow(KEY_UPNEXT_FOLLOWED_ONLY) { upNextFollowedOnly }
    }

    private inline fun <T> createPreferenceFlow(
        key: String,
        crossinline getValue: () -> T,
    ): Flow<T> = preferenceKeyChangedFlow
        // Emit on start so that we always send the initial value
        .onStart { emit(key) }
        .filter { it == key }
        .map { getValue() }
        .distinctUntilChanged()

    private val AppPreferences.Theme.storageKey: String
        get() = when (this) {
            AppPreferences.Theme.LIGHT -> context.getString(R.string.pref_theme_light_value)
            AppPreferences.Theme.DARK -> context.getString(R.string.pref_theme_dark_value)
            AppPreferences.Theme.SYSTEM -> context.getString(R.string.pref_theme_system_value)
        }

    private fun getThemeForStorageValue(value: String) = when (value) {
        context.getString(R.string.pref_theme_light_value) -> AppPreferences.Theme.LIGHT
        context.getString(R.string.pref_theme_dark_value) -> AppPreferences.Theme.DARK
        else -> AppPreferences.Theme.SYSTEM
    }
}
