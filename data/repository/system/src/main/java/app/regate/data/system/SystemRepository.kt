package app.regate.data.system

import app.regate.data.daos.UserDao
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.data.dto.system.ReportData
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SystemRepository(
    private val systemDataSourceImpl: SystemDataSourceImpl,
    private val userDao: UserDao
){
    suspend fun sendReport(d:ReportData) {
        val profileId = try {
            userDao.getUser(0).profile_id
        }catch (e:Exception){
            0
        }
        systemDataSourceImpl.sendReport(d.copy(
            profile_id = profileId
        ))
    }

}