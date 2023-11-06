package app.regate.data.daos

import app.regate.models.Profile
import app.regate.models.ProfileCategory
import kotlinx.coroutines.flow.Flow

interface ProfileDao:EntityDao<Profile> {
    fun observeProfile(id:Long):Flow<Profile>
    fun observeProfileSalas(ids:List<Long>):Flow<List<Profile>>
    fun insertProfileCategories(entities:List<ProfileCategory>)
}