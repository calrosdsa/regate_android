package app.regate.media

import android.content.Context
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.account.AccountRepository
import app.regate.data.auth.AuthRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import java.util.Locale

@Inject
class SettingViewModel(
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    private val authRepository: AuthRepository,
    private  val accountRepository: AccountRepository,
    private val appPreferences: AppPreferences
    ):ViewModel() {
    private val loadingState = ObservableLoadingCounter()
//    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<SettingState> = combine(
        loadingState.observable,
        observeUser.flow,
        observeAuthState.flow,
        appPreferences.observeTheme(),
        appPreferences.observeUseDynamicColors()
    ){loading,user,authState,theme,useDynamicColors ->
        SettingState(
            loading = loading,
            user = user,
            authState = authState,
            theme = theme,
            useDynamicColors = useDynamicColors
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SettingState.Empty
    )
    init {
        observeUser(Unit)
        observeAuthState(Unit)
    }
    fun logout(){
        viewModelScope.launch {
            authRepository.clearAuth()
            accountRepository.clearAuthData()
        }
    }
    fun updateThemePreference(theme: AppPreferences.Theme){
        appPreferences.theme = theme
    }
    fun updateDynamicColors(useDynamicColors:Boolean){
        appPreferences.useDynamicColors = useDynamicColors
    }
    fun updateLanguagePreference(context:Context){
        val overrideConfiguration = context.resources.configuration
        overrideConfiguration.setLocale(Locale.ENGLISH)
//        val contextWrapper = context.createConfigurationContext(overrideConfiguration)

//        context.resources.updateConfiguration(conf,context.resources.displayMetrics)
//        val appLocale  = LocaleListCompat.forLanguageTags("en")
    }
}