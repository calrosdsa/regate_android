/*
 * Copyright 2023 Google LLC
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

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import app.regate.tasks.works.InitSync
import app.regate.tasks.works.SyncLibraryShows
import app.regate.tasks.works.SyncMessages
import me.tatarka.inject.annotations.Inject

@Inject
class AppWorkerFactory(
    private val syncLibraryShows: (Context, WorkerParameters) -> SyncLibraryShows,
    private val initSync: (Context, WorkerParameters) -> InitSync,
    ) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? = when (workerClassName) {
        name<SyncLibraryShows>() -> syncLibraryShows(appContext, workerParameters)
        name<InitSync>() -> initSync(appContext,workerParameters)
        name<SyncMessages>() -> initSync(appContext,workerParameters)
        else -> null
    }

    private inline fun <reified C> name() = C::class.qualifiedName
}
