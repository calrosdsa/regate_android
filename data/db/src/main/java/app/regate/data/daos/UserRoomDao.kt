package app.regate.data.daos

import app.regate.compoundmodels.UserProfileRoom
import app.regate.models.UserRoom
import kotlinx.coroutines.flow.Flow

interface UserRoomDao:EntityDao<UserRoom> {
    fun observeUsersRoom(id:Long): Flow<List<UserProfileRoom>>
}