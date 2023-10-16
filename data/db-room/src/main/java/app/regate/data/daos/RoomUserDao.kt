package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfile
import app.regate.models.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserDao:UserDao, RoomEntityDao<User> {
    @Transaction
    @Query("SELECT * FROM users limit 1")
    abstract override fun observeUser():Flow<User>

    @Query("SELECT * FROM USERS WHERE id = :id ")
    abstract override suspend fun getUser(id: Long): User

    @Query("SELECT * FROM USERS  limit 1 ")
    abstract override fun observeUserAndProfile(): Flow<UserProfile>


    @Query("DELETE FROM users")
    abstract override suspend fun deleteUser()
}