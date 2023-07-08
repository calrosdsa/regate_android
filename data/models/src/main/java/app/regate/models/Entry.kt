package app.regate.models

interface Entry : AppEntity

interface MultipleEntry : Entry {
    val otherShowId: Long
}

interface PaginatedEntry : Entry {
    val page: Int
}