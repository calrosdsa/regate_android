package app.regate.data.coin

import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.data.dto.empresa.labels.LabelDto

interface CoinDataSource {
   suspend fun getUserBalance():UserBalance
   suspend fun getRecargaCoins():List<RecargaCoinDto>

   suspend fun getToken():TokenQrReponse

   suspend fun getQr(d:QrRequest,token:String):QrResponse
}
