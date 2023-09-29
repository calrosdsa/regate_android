package app.regate.domain

import app.regate.data.common.AddressDevice
import app.regate.data.common.getDataEntityFromJson
import app.regate.settings.AppPreferences
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject

@Inject
class Converter(
   private val preference:AppPreferences,
   private val dispatchers: AppCoroutineDispatchers
) {
    fun observeAddress(): Flow<AddressDevice?> {
        return preference.observeAddress().flowOn(dispatchers.computation).mapLatest {
            if(it.isNotBlank()){
            getDataEntityFromJson(it)
            }else{
                null
            }
        }
    }
    fun getCategories():List<Long>?{
        return try{
             getDataEntityFromJson<List<Long>>(preference.categories)
        }catch (e:Exception){
            null
        }
    }
    fun getAddress():AddressDevice?{
        return getDataEntityFromJson(preference.address)
    }
}