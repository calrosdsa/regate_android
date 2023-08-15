package app.regate.data.system

import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SystemRepository(
    private val coinDataSourceImpl: SystemDataSourceImpl
){
    suspend fun getUserBalance():UserBalance {
        return coinDataSourceImpl.getUserBalance()
    }
    suspend fun getRecargaCoins():List<RecargaCoinDto>{
        return coinDataSourceImpl.getRecargaCoins()
    }

    suspend fun getToken():TokenQrReponse{
        return coinDataSourceImpl.getToken()
    }

    suspend fun getQr(d:QrRequest,token:String):QrResponse{
        return coinDataSourceImpl.getQr(d,token)
    }
}