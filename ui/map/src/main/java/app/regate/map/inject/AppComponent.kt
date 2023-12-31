/*
 * Copyright 2017 Google LLC
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

package app.regate.map.inject

import android.app.Application
import app.regate.app.ApplicationInfo
import app.regate.appinitializers.AppInitializer
import app.regate.util.AppCoroutineDispatchers
import app.regate.inject.ApplicationScope
import kotlinx.coroutines.Dispatchers
//import android.content.pm.ApplicationInfo

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides



interface AppComponent {
    @ApplicationScope
    @Provides
    fun provideApplicationId(application: Application): ApplicationInfo {
        return ApplicationInfo(application.packageName)
    }



    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )

//
//    @Provides
//    @IntoSet
//    fun providePreferencesInitializer(bind: PreferencesInitializer): AppInitializer = bind
//
//    @Provides
//    @IntoSet
//    fun provideTmdbInitializer(bind: TmdbInitializer): AppInitializer = bind
}
