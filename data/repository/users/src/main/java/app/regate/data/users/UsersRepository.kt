package app.regate.data.users

import app.regate.data.daos.ProfileDao
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.mappers.DtoToProfile
import app.regate.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class UsersRepository(
    private val usersDataSourceImpl: UsersDataSourceImpl,
    private val profileDao: ProfileDao,
    private val profileDtoToProfile: DtoToProfile
){
    suspend fun getProfile(id:Long):ProfileDto{
        return usersDataSourceImpl.getProfile(id).also {
            profileDao.upsert(profileDtoToProfile.map(it))
        }
    }
}