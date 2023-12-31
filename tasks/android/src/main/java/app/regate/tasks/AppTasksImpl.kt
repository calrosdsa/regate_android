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

package app.regate.tasks

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import app.regate.extensions.fluentIf
import app.regate.tasks.works.InitSync
import app.regate.tasks.works.SyncLibraryShows
import app.regate.tasks.works.SyncMessages
import java.util.concurrent.TimeUnit
import me.tatarka.inject.annotations.Inject

@Inject
class AppTasksImpl(
    private val workManager: WorkManager,
) : AppTasks {
//    override fun syncLibraryShows(deferUntilIdle: Boolean) {
//        val request = OneTimeWorkRequestBuilder<SyncLibraryShows>()
//            .addTag(SyncLibraryShows.TAG)
//            .fluentIf(deferUntilIdle) {
//                setConstraints(
//                    Constraints.Builder()
//                        .setRequiresDeviceIdle(true)
//                        .build(),
//                )
//            }
//            .build()
//        workManager.enqueue(request)
//    }
//
//    override fun setupNightSyncs() {
//        val nightlyConstraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresCharging(true)
//            .build()
//
//        workManager.enqueueUniquePeriodicWork(
//            SyncLibraryShows.NIGHTLY_SYNC_TAG,
//            ExistingPeriodicWorkPolicy.KEEP,
//            PeriodicWorkRequestBuilder<SyncLibraryShows>(24, TimeUnit.HOURS)
//                .setConstraints(nightlyConstraints)
//                .build(),
//        )
//    }

    override fun syncMessages() {
//        val nightlyConstraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresCharging(true)
//            .build()
//        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS

        workManager.enqueueUniquePeriodicWork(
            SyncMessages.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<SyncMessages>(1, TimeUnit.HOURS)
//                .setConstraints(nightlyConstraints)
                .build(),
        )
    }
    override fun initSync() {
        val request = OneTimeWorkRequestBuilder<InitSync>()
            .addTag(InitSync.TAG)
            .setConstraints(
                    Constraints.Builder()
//                        .setRequiresDeviceIdle(true)
                        .build(),
                )
            .build()
        workManager.enqueue(request)
    }
}
