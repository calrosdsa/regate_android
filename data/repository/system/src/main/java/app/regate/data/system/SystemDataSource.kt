package app.regate.data.system

import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.data.dto.system.NotificationCount
import app.regate.data.dto.system.NotificationDto
import app.regate.data.dto.system.ReportData
import app.regate.models.Notification

interface SystemDataSource {
   suspend fun sendReport(d:ReportData)
   suspend fun getNotifications():List<NotificationDto>
   suspend fun getNotificationCount():NotificationCount
}

