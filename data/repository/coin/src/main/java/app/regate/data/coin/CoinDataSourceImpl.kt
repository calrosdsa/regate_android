package app.regate.data.coin

import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.labels.LabelDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.tatarka.inject.annotations.Inject

@Inject
class CoinDataSourceImpl(
    private val client:HttpClient,
//    private val authStore: AuthStore
): CoinDataSource {
    override suspend fun getRecargaCoins(): List<RecargaCoinDto> {
        return client.get("/v1/coin/list/").body()
    }

}

