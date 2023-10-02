package app.regate.data.users

import app.regate.data.dto.FileData
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.PaginationProfilesResponse
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse


interface UsersDataSource {
   suspend fun getProfile(id:Long):ProfileDto
   suspend fun editProfile(d:ProfileDto,file:FileData?):ProfileDto
   suspend fun searchUsers(d: SearchFilterRequest, page:Int, size: Int): PaginationProfilesResponse
}

