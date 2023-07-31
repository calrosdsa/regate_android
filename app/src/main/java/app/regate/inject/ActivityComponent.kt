/*
 * Copyright 2022 Google LLC
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

package app.regate.inject

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.registerForActivityResult
import androidx.core.os.ConfigurationCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.Locale
import app.regate.R
import app.regate.home.MainActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import me.tatarka.inject.annotations.Provides

interface ActivityComponent {

//    @Provides
//    fun enableLocation(activity: Activity) {
//        val context = activity as MainActivity
//        val launcher =
//        val locationRequest = LocationRequest.create()
//        locationRequest.apply {
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            interval = 30 * 1000.toLong()
//            fastestInterval = 5 * 1000.toLong()
//        }
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//        builder.setAlwaysShow(true)
//        val result=
//            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
//        result.addOnCompleteListener {
//            try {
//                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
////                println("location>>>>>>> ${response.locationSettingsStates.isGpsPresent}")
//                if(response.locationSettingsStates?.isGpsPresent!!){
//                    Log.d("DEBUG_APP_SUCC", response.locationSettingsStates!!.isGpsPresent.toString())
//                    //TODO()
//                }
//                //do something
//            }catch (e: ApiException){
//                Log.d("DEBUG_APP_LOC",e.localizedMessage?:"")
//                when (e.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
//                        val intentSenderRequest =
//                            e.status.resolution?.let { it1 -> IntentSenderRequest.Builder(it1).build() }
//
//                    } catch (e: IntentSender.SendIntentException) {
//                        //TODO()
//                    }
//                }
//            }
//        }
//    }
    @Provides
    fun provideActivityLocale(activity: Activity): Locale {
        return ConfigurationCompat.getLocales(activity.resources.configuration)
            .get(0) ?: Locale.getDefault()
    }

    @Provides
    fun getGoogleLoginAuth(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
//            .requestIdToken(activity.getString(R.string.gcp_id))
            .requestId()
            .requestProfile()
            .build()
        return  GoogleSignIn.getClient(activity, gso)
    }
}
