package app.regate.data.coin

import app.regate.data.daos.LabelDao
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.mappers.AmenityToLabel
import app.regate.data.mappers.CategoryToLabel
import app.regate.data.mappers.RuleToLabel
import app.regate.data.mappers.SportsToLabel
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class CoinRepository(
    private val coinDataSourceImpl: CoinDataSourceImpl
){
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