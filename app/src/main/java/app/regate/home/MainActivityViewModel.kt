package app.regate.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.constant.Host
import app.regate.data.account.AccountRepository
import app.regate.data.chat.ChatRepository
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.ws.PayloadUserBalanceUpdate
import app.regate.data.dto.account.ws.PayloadWsAccountType
import app.regate.data.dto.account.ws.WsAccountPayload
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.system.NotificationDto
import app.regate.data.system.SystemRepository
import app.regate.domain.observers.account.ObserveUser
import app.regate.settings.AppPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.util.InternalAPI
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@OptIn(InternalAPI::class)
@Inject
class MainActivityViewModel(
    private val accountRepository:AccountRepository,
    private val preferences:AppPreferences,
    private val client: HttpClient,
    private val coinRepository: CoinRepository,
    private val chatRepository: ChatRepository,
    private val systemRepository: SystemRepository,
    private val observeUser: ObserveUser,
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
        observeUser(Unit)
        viewModelScope.launch {
            observeUser.flow.collect {
                try {
                    if(it.profile_id != 0L){
                        startWs(it.profile_id)
                    }
                    Log.d("DEBUG_APP_USER",it.toString())
                }catch (e:Exception){
                    Log.d("DEBUG_APP_USER_ERR",e.localizedMessage?:"")
                }
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun startWs(profileId:Long){
        try {
            val cl = client.webSocketSession(method = HttpMethod.Get, host = Host.host,
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port = Host.port, path = "v1/ws/suscribe/user/?id=${profileId}")
            Log.d("DEBUG_APP","start ws.......")
            Log.d("DEBUG_APP_WS",cl.isActive.toString())
            if(cl.isActive){
                chatRepository.syncMessages()
            }
          cl.apply{
                  for (message in incoming) {
                      message as? Frame.Text ?: continue
                      val data = Json.decodeFromString<WsAccountPayload>(message.readText())
                      Log.d("DEBUG_APP",message.readText())
                      when (data.type){
                          PayloadWsAccountType.PAYLOAD_USER_BALANCE.ordinal->{
                              Log.d("DEBUG_APP",message.readText())
                              val payload = Json.decodeFromString<PayloadUserBalanceUpdate>(data.payload)
                              coinRepository.updateUserBalance(payload)
                          }
                          PayloadWsAccountType.PAYLOAD_GRUPO_MESSAGE.ordinal ->{
                              val payload = Json.decodeFromString<GrupoMessageDto>(data.payload)
                              chatRepository.saveMessageIgnoreOnConflict(payload,false)
                              Log.d("DEBUG_APP_WS_USER",payload.toString())
//                              db.myGroupsDao().updateLastMessageGrupo(grupo.id,lastMessage.content,lastMessage.created_at)
                          }
                          PayloadWsAccountType.PAYLOAD_TYPE_NOTIFICATION.ordinal -> {
                              try{
                              val payload = Json.decodeFromString<NotificationDto>(data.payload)
                              systemRepository.insertNotification(payload)
                              Log.d("DEBUG_APP",payload.toString())
                              }catch(e:Exception){
                                  Log.d("DEBUG_APP_ER" , e.localizedMessage?:"")
                              }
                          }
                          else -> {}
                      }
                  }

          }
            cl.start(emptyList())
        }catch (e:Exception){
            delay(1000)
            Log.d("DEBUG_ARR",e.localizedMessage?:"")
            startWs(profileId)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun DefaultClientWebSocketSession.outputMessage(){
                send(
                    Json.encodeToString("DMASKDM KAS")
                )
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
