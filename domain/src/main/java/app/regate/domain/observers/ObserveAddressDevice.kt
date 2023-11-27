package app.regate.domain.observers

import app.regate.data.auth.AppAuthState
import app.regate.data.auth.AuthRepository
import app.regate.data.common.AddressDevice
import app.regate.data.common.getDataEntityFromJson
import app.regate.domain.SubjectInteractor
import app.regate.settings.AppPreferences
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAddressDevice(
    private val appPreferences: AppPreferences,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<Unit, AddressDevice?>() {
    override fun createObservable(params: Unit): Flow<AddressDevice?> {
        return appPreferences.observeAddress().map {data->
            try{
            Json.decodeFromString(data)
            }catch (e:Exception){
                null
            }

        }
    }
}
