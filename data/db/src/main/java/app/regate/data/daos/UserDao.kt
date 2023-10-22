package app.regate.data.daos

import app.regate.compoundmodels.UserProfile
import app.regate.models.account.User
import app.regate.models.account.UserBalance
import kotlinx.coroutines.flow.Flow

interface UserDao :EntityDao<User>{
    fun observeUser(): Flow<User>
    suspend fun getUser(id:Long): User
    suspend fun deleteUser()
    fun observeUserBalance():Flow<UserBalance>
    fun updateUserBalance(profileId:Long,amount:Double,shouldAdd:Boolean)
    suspend fun insetUserBalance(entity:UserBalance)
    fun observeUserAndProfile():Flow<UserProfile>
}