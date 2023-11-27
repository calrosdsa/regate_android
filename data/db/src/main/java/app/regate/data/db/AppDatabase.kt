package app.regate.data.db

import app.regate.data.daos.ChatDao
import app.regate.data.daos.CupoDao
import app.regate.data.daos.EmojiDao
import app.regate.data.daos.EstablecimientoDao
import app.regate.data.daos.FavoriteEstablecimientoDao
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.InstalacionDao
import app.regate.data.daos.LabelDao
import app.regate.data.daos.LastUpdatedEntityDao
import app.regate.data.daos.MessageInboxDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.MessageSalaDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.NotificationDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.ReservaDao
import app.regate.data.daos.SearchHistoryDao
import app.regate.data.daos.UserDao
import app.regate.data.daos.UserGrupoDao
import app.regate.data.daos.UserRoomDao

interface AppDatabase {
    fun establecimientoDao(): EstablecimientoDao
    fun instalacionDao():InstalacionDao
    fun cupoDao():CupoDao
    fun userDao():UserDao
    fun profileDao():ProfileDao
    fun messageProfileDao():MessageProfileDao
    fun labelsDao():LabelDao
    fun grupoDao():GrupoDao
    fun userGrupoDao():UserGrupoDao
    fun userRoomDao():UserRoomDao
    fun myGroupsDao():MyGroupsDao
    fun favoriteEstablecimientos():FavoriteEstablecimientoDao
    fun messageInboxDao():MessageInboxDao
    fun reservaDao():ReservaDao
    fun notificationDao():NotificationDao
    fun messageSalaDao():MessageSalaDao
    fun searchHistoryDao():SearchHistoryDao
    fun emojiDao():EmojiDao
    fun chatDao():ChatDao
    fun lastUpdatedEntityDao():LastUpdatedEntityDao
}