package app.regate.data.users

import app.regate.data.daos.ProfileDao
import app.regate.data.dto.FileData
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.PaginationProfilesResponse
import app.regate.data.dto.account.user.ProfileCategoryRequest
import app.regate.data.dto.account.user.ProfileDetailDto
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.mappers.DtoToProfile
import app.regate.inject.ApplicationScope
import app.regate.models.Profile
import app.regate.models.ProfileCategory
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
    suspend fun updateCategoriesProfile(d:List<ProfileCategoryRequest>,profileId:Long){
        withContext(dispatchers.io){
            try {
                usersDataSourceImpl.updateCategoriesProfile(d)
                val filterList = d.filter { !it.should_delete }
                val profileCategories = filterList.map {result->
                    ProfileCategory(
                        profile_id = profileId,
                        category_id = result.category_id
                    )
                }
                profileDao.deleteAllProfileCategories(profileId)
                profileDao.insertProfileCategories(profileCategories)
            }catch (e:Exception){
                throw e
            }
        }
    }
    suspend fun searchProfiles(d:SearchFilterRequest,page:Int=1,size:Int=5):PaginationProfilesResponse{
        return usersDataSourceImpl.searchUsers(d,page,size)
    }
    suspend fun getProfile(id:Long){
        withContext(dispatchers.io){
         try{
         usersDataSourceImpl.getProfile(id).also {result->
            profileDao.upsert(profileDtoToProfile.map(result.profile))
             val profileCategories = result.categories.map{categorie->
                 ProfileCategory(
                     profile_id = result.profile.profile_id,
                     category_id = categorie
                 )
             }
             profileDao.insertProfileCategories(profileCategories)
        }
         }catch (e:Exception){
             throw e
         }
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