package app.regate.data.coin

import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.labels.LabelDto

interface CoinDataSource {
   suspend fun getRecargaCoins():List<RecargaCoinDto>
}

