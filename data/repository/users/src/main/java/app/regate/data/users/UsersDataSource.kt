package app.regate.data.users

import app.regate.data.dto.FileData
import app.regate.data.dto.account.user.ProfileDto


interface UsersDataSource {
   suspend fun getProfile(id:Long):ProfileDto
   suspend fun editProfile(d:ProfileDto,file:FileData?):ProfileDto
}

