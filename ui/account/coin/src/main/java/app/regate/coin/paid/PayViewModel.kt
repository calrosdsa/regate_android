package app.regate.coin.paid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.coin.CoinRepository
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.util.AppMedia
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


@Inject
class PayViewModel(
    @Assisted savedState:SavedStateHandle,
    private val coinRepository: CoinRepository,
    private val appMedia:AppMedia
):ViewModel() {
    private val qrR = savedState.get<String>("qrRequest")
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val qr = MutableStateFlow<Bitmap?>(null)
    private val qrData = MutableStateFlow<QrRequest?>(null)
    val state: StateFlow<PayState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        qr,
        qrData
    ) { loading, message, qr ,qrData->
        PayState(
            loading = loading,
            message = message,
            qr = qr,
            qrData = qrData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PayState.Empty
    )

    init {
        getQr()
    }

    fun getQr() {
        viewModelScope.launch {
            try {
                if(!qrR.isNullOrBlank()){
                loadingCounter.addLoader()
                val res = coinRepository.getToken()
                val qrRequest = Json.decodeFromString<QrRequest>(qrR)
                qrData.tryEmit(qrRequest)
//                    QrRequest(
//                    currency = "BOB",
//                    gloss = "Prueba QR",
//                    amount = 1,
//                    singleUse = true,
//                    expirationDate = "2023-08-15",
//                    additionalData = "Datos Adicionales para identificar el QR",
//                    destinationAccountId = "1"
//                )
                val qrRes = coinRepository.getQr(qrRequest, res.message)
                val decodedString: ByteArray = Base64.decode(qrRes.qr, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                qr.tryEmit(decodedByte)
                Log.d("DEBUG_APP_RES", res.message)
                Log.d("DEBUG_APP_RES", qrRes.id)
//                delay(2000)
                loadingCounter.removeLoader()
                }else{
                    Log.d("DEBUG_APP_RES", "INVALID REQUEST DATA")
                }
            } catch (e: Exception) {
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERR", e.localizedMessage ?: "")
            }
        }
    }


    fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        appMedia.saveImage(bitmap,context,folderName)
    }



    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}


