package app.regate.coin.paid

import android.graphics.Bitmap
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.coin.QrRequest

data class PayState(
    val loading:Boolean = true,
    val message:UiMessage? = null,
    val qr :Bitmap? = null,
    val qrData :QrRequest? = null
){
    companion object{
        val Empty = PayState()
    }
}


