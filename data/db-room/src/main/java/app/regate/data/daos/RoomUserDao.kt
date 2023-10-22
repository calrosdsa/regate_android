package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.UserProfile
import app.regate.models.account.User
import app.regate.models.account.UserBalance
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomUserDao:UserDao, RoomEntityDao<User> {
    @Transaction
    @Query("SELECT * FROM users limit 1")
    abstract override fun observeUser():Flow<User>

    @Query("SELECT * FROM USERS WHERE id = :id ")
    abstract override suspend fun getUser(id: Long): User

    @Transaction
    @Query("SELECT * FROM USERS  limit 1 ")
    abstract override fun observeUserAndProfile(): Flow<UserProfile>
    @Transaction
    @Query("SELECT * FROM user_balance  limit 1 ")
    abstract override fun observeUserBalance(): Flow<UserBalance>
    @Query("""
        update user_balance set coins = case when :shouldAdd then coins + :amount else coins - :amount end 
        where profile_id = :profileId
            """)
    abstract override fun updateUserBalance(profileId: Long, amount: Double, shouldAdd: Boolean)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insetUserBalance(entity: UserBalance)
    @Query("DELETE FROM users")
    abstract override suspend fun deleteUser()
}