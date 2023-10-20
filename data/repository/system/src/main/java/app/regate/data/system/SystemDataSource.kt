package app.regate.data.system

import app.regate.data.dto.system.NotificationCount
import app.regate.data.dto.system.NotificationDto
import app.regate.data.dto.system.ReportData

interface SystemDataSource {
   suspend fun sendReport(d:ReportData)
   suspend fun getNotifications():List<NotificationDto>
   suspend fun getNotificationCount():NotificationCount
}

