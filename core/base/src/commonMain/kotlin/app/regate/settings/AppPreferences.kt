/*
 * Copyright 2019 Google LLC
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

import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    fun setup()

    var startRoute:String

    var categories:String

    var keyBoardHeight:Int

    var filter:String
    fun observeFilter(): Flow<String>

    var address:String
    fun observeAddress(): Flow<String>

    var fcmToken: String
    fun observeFcmToken(): Flow<String>

    var token: String
    fun observeToken(): Flow<String>

    var theme: Theme
    fun observeTheme(): Flow<Theme>

    var useDynamicColors: Boolean
    fun observeUseDynamicColors(): Flow<Boolean>

    var useLessData: Boolean
    fun observeUseLessData(): Flow<Boolean>

    var libraryFollowedActive: Boolean
    fun observeLibraryFollowedActive(): Flow<Boolean>

    var libraryWatchedActive: Boolean
    fun observeLibraryWatchedActive(): Flow<Boolean>

    var upNextFollowedOnly: Boolean
    fun observeUpNextFollowedOnly(): Flow<Boolean>
    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM,
    }

}
