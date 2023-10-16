package app.regate.data.daos

import app.regate.compoundmodels.UserProfile
import app.regate.models.User
import kotlinx.coroutines.flow.Flow

interface UserDao :EntityDao<User>{
    fun observeUser(): Flow<User>
    suspend fun getUser(id:Long):User
    suspend fun deleteUser()
    fun observeUserAndProfile():Flow<UserProfile>
}