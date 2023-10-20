package app.regate.data.coin

import app.regate.data.daos.UserDao
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalanceDto
import app.regate.inject.ApplicationScope
import app.regate.models.account.UserBalance
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class CoinRepository(
    private val coinDataSourceImpl: CoinDataSourceImpl,
    private val userDao:UserDao,
    private val dispatchers:AppCoroutineDispatchers
){
    suspend fun getUserBalance() {
        withContext(dispatchers.computation){
            try{
             val res = coinDataSourceImpl.getUserBalance()
                userDao.insetUserBalance(
                    UserBalance(
                        balance_id = res.balance_id.toLong(),
                        coins = res.coins,
                        profile_id = res.profile_id.toLong()
                    )
                )
            }catch (e:Exception){
                throw e
            }
        }
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