package app.regate.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.settings.AppPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class MainActivityViewModel(
    private val accountRepository:AccountRepository,
    private val preferences:AppPreferences
): ViewModel() {

    init {
        Log.d("DEBUG_",preferences.fcmToken)
        Log.d("DEBUG_",preferences.fcmToken.isBlank().toString())
        if(preferences.fcmToken.isBlank()){
        logRegToken()
        }else{
            viewModelScope.launch {
                Log.d("DEBUG_","UPDATING FCM TOKEN")
                accountRepository.updateFcmToken(preferences.fcmToken)
            }
        }
        viewModelScope.launch {
            while(true){
                delay(1000)
                Log.d("DEBUG_APP","runn")
            }
        }
    }

    fun logRegToken() {

        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("DEBUG_", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            if(preferences.fcmToken.isBlank()){
                viewModelScope.launch {
                    try{

                preferences.fcmToken = token
                accountRepository.saveFcmToken(FcmRequest(
                    fcm_token = token,
                    category_id = 1
                ))
                    }catch(e :Exception){
                        Log.d("DEBUG_",e.localizedMessage?:"")
                    }
                }
            }
            // Log and toast
            val msg = "FCM Registration token: $token"
            Log.d("DEBUG_", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
        // [END log_reg_token]
    }



}
