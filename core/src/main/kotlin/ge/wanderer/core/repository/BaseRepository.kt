package ge.wanderer.core.repository

interface BaseRepository<T> {
    fun findById(id: Long): T
    fun persist(model: T): T
    fun delete(model: T)
}