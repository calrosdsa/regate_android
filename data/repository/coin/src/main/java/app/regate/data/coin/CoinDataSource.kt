package app.regate.data.coin

import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalanceDto

interface CoinDataSource {
   suspend fun getUserBalance():UserBalanceDto
   suspend fun getRecargaCoins():List<RecargaCoinDto>

   suspend fun getToken():TokenQrReponse

   suspend fun getQr(d:QrRequest,token:String):QrResponse
}

