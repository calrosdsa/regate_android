package app.regate.data.daos

import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert

interface RoomEntityDao<in E>:EntityDao<E> {
    @Upsert
    override suspend fun upsert(entity: E): Long

    @Upsert
    override suspend fun upsertAll(vararg entity: E)

    @Upsert
    override suspend fun upsertAll(entities: List<E>)

    @Update
    override suspend fun update(entity: E)

    @Delete
    override suspend fun deleteEntity(entity: E): Int
}