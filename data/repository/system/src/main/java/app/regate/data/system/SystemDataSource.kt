package app.regate.data.system

import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance

interface SystemDataSource {
   suspend fun getUserBalance():UserBalance
   suspend fun getRecargaCoins():List<RecargaCoinDto>

   suspend fun getToken():TokenQrReponse

   suspend fun getQr(d:QrRequest,token:String):QrResponse
}

