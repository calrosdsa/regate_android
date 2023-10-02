package app.regate.data.users

import app.regate.data.daos.ProfileDao
import app.regate.data.dto.FileData
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.PaginationProfilesResponse
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.mappers.DtoToProfile
import app.regate.inject.ApplicationScope
import app.regate.models.Profile
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class UsersRepository(
    private val usersDataSourceImpl: UsersDataSourceImpl,
    private val profileDao: ProfileDao,
    private val profileDtoToProfile: DtoToProfile,
    private val dispatchers: AppCoroutineDispatchers,
    private val profileMapper:DtoToProfile
){
    suspend fun searchProfiles(d:SearchFilterRequest,page:Int=1,size:Int=5):PaginationProfilesResponse{
        return usersDataSourceImpl.searchUsers(d,page,size)
    }
    suspend fun getProfile(id:Long):ProfileDto{
        return usersDataSourceImpl.getProfile(id).also {
            profileDao.upsert(profileDtoToProfile.map(it))
        }
    }
    suspend fun editProfile(d:Profile,file:FileData?):ProfileDto{
        val profile = ProfileDto(
            profile_id = d.id,
            nombre = d.nombre,
            apellido = d.apellido,
            user_id = d.user_id?:0,
            email = d.email?:"",
            created_at = d.created_at,
            profile_photo = d.profile_photo
        )
        return usersDataSourceImpl.editProfile(profile,file).also {
            withContext(dispatchers.computation){
                profileDao.upsert(profileMapper.map(it))
            }
        }
    }
}