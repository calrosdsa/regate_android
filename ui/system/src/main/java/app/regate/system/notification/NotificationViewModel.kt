package app.regate.system.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.constant.MainPages
import app.regate.constant.Route
import app.regate.data.system.SystemRepository
import app.regate.domain.observers.system.ObserveNotifications
import app.regate.settings.AppPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class NotificationViewModel(
 observeNotifications: ObserveNotifications,
   private val systemRepository: SystemRepository,
   private val preferences: AppPreferences,
 ) : ViewModel() {
 // val loadingCounter = ObservableLoadingCounter()
// val uiMessageManager = UiMessageManager()
 val state: StateFlow<NotificationState> = combine(
  observeNotifications.flow,
//  observeUnreadNotificationCount.flow
 ) { notifications ->
  NotificationState(
   notifications = notifications[0]
  )
 }.stateIn(
  scope = viewModelScope,
  started = SharingStarted.WhileSubscribed(),
  initialValue = NotificationState.Empty
 )

 init {
  observeNotifications(Unit)
//  observeUnreadNotificationCount(Unit)
  updateUnreadNotifications()


//  getData()
  updatePreferences()
 }

 private fun updatePreferences() {
  preferences.startRoute = MainPages.Notifications
 }

// fun getData(){
//  viewModelScope.launch {
//   try{
//       systemRepository.getNotifications()
//   }catch (e:Exception){
//      Log.d("DEBUG_APP_ERR",e.localizedMessage?:"")
//   }
//  }
// }

 private fun updateUnreadNotifications() {
  viewModelScope.launch {
   try {
    systemRepository.updateUnreadNotifications()
   } catch (e: Exception) {
    Log.d("DEBUG_APP", e.localizedMessage ?: "")
   }
  }
 }

}