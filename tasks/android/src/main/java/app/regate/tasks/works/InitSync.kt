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
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.regate.constant.Route
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
import app.regate.common.resources.R
import kotlin.random.Random


@Inject
class InitSync(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val labelRepository: LabelRepository,
//    private val establecimientoRepository: EstablecimientoRepository
//    private val updateLibraryShows: UpdateLibraryShows,
//    private val logger: Logger,
) : CoroutineWorker(context, params) {
    companion object {
        internal const val TAG = "init-sync"
//        internal const val NIGHTLY_SYNC_TAG = "night-sync-all-followed-shows"
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        try{
            withContext(Dispatchers.IO) {
            val task1 = async{
                try{
                labelRepository.getAmenities()
                }catch (e:Exception){
                    //TODO()
                }
            }
            val task2 =  async{
                try {
                labelRepository.getCategories()
                }catch (e:Exception){
                    //TODO()
                }
            }
            val task3 = async{
                try{
                labelRepository.getSports()
                }catch (e:Exception){
                    //TODO()
                }
            }
            val task4 = async{
                try{
                labelRepository.getRules()
                }catch(e:Exception){
                    //TODO()
                }
            }
            val task5 = async{
                try{
                labelRepository.getEmojis()
                }catch(e:Exception){
                    //TODO()
                }
            }
//            val task4 = async{ establecimientoRepository.getEstablecimientos()}
                awaitAll(task1,task2,task3,task4,task5)
        }

        }catch (e:Exception){
            Log.d("DEBUG_APP_SYNC",e.localizedMessage?:"")
            return Result.failure()
        }
        return Result.success()
    }

}
