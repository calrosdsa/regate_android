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

import app.regate.appinitializers.AppInitializer
import me.tatarka.inject.annotations.Inject

@Inject
class AppTasksInitializer(
    private val appTasks: Lazy<AppTasks>,
) : AppInitializer {
    override fun init() {
//        appTasks.value.setupNightSyncs()
        appTasks.value.initSync()
        appTasks.value.syncMessages()
    }
}
