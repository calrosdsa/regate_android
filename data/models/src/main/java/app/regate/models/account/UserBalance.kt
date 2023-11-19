package app.regate.models.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.models.AppEntity

@Entity(
    tableName = "user_balance"
)
data class UserBalance(
    @PrimaryKey(autoGenerate = false)
    val balance_id: Long,
    val profile_id:Long,
    val coins:Double,
    val retain_coin:Double =0.0,
)