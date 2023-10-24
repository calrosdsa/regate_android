package app.regate.data.daos

interface EntityDao<in E> {
    suspend fun upsert(entity: E): Long

    suspend fun upsertAll(vararg entity: E)

    suspend fun upsertAll(entities: List<E>)
    suspend fun insertOnConflictIgnore(entities: E)
    suspend fun insertAllonConflictIgnore(entities: List<E>)

    suspend fun update(entity: E)
    suspend fun deleteEntity(entity: E): Int
}