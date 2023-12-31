/*
 * Copyright 2020 Google LLC
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

package app.regate.tasks

import android.app.Application
import androidx.work.WorkManager
import app.regate.appinitializers.AppInitializer
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface TasksComponent {
    @ApplicationScope
    @Provides
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }

    @ApplicationScope
    @Provides
    @IntoSet
    fun provideShowTasksInitializer(bind: AppTasksInitializer): AppInitializer = bind

    @ApplicationScope
    @Provides
    fun provideShowTasks(bind: AppTasksImpl): AppTasks = bind
}
