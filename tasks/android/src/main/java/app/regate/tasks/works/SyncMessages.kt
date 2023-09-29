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

package app.regate.tasks.works

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.regate.common.resources.R
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.labels.LabelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.util.Locale
import kotlin.random.Random

@Inject
class SyncMessages(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val labelRepository: LabelRepository,
//    private val establecimientoRepository: EstablecimientoRepository
//    private val updateLibraryShows: UpdateLibraryShows,
//    private val logger: Logger,
) : CoroutineWorker(context, params) {
    companion object {
        internal const val TAG = "sync-messages"
//        internal const val NIGHTLY_SYNC_TAG = "night-sync-all-followed-shows"
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        try{
           Log.d("DEBUG_APP","sync....")
            try{
                val CHANNEL_ID = applicationContext.getString(R.string.notification_billing_channel)
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setContentTitle("TEST NOTIFICATION")
                    .setContentText("Notification context")
                    .setSmallIcon(R.drawable.logo_app)
                    .setAutoCancel(true)
                    .build()

                with(NotificationManagerCompat.from(applicationContext)) {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val id = Random.nextInt(100)
                        notify(id, notification)
                    }
                }
            }catch(e:Exception){
                Log.d(InitSync.TAG,e.localizedMessage?:"")
            }
        }catch (e:Exception){
            return Result.failure()
        }
        return Result.success()
    }

}
