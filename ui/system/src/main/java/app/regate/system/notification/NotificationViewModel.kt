package app.regate.system.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.system.SystemRepository
import app.regate.domain.observers.ObserveNotifications
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
) : ViewModel() {
 // val loadingCounter = ObservableLoadingCounter()
// val uiMessageManager = UiMessageManager()
 val state: StateFlow<NotificationState> = combine(
  observeNotifications.flow
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
  updateUnreadNotifications()
 }

 private fun updateUnreadNotifications(){
  viewModelScope.launch {
   try{
    systemRepository.updateUnreadNotifications()
   }catch(e:Exception){
    Log.d("DEBUG_APP",e.localizedMessage?:"")
   }
  }
 }

}