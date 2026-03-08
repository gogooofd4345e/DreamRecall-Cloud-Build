package com.dreamrecall.data

import kotlinx.coroutines.flow.Flow

class DreamRepository(private val dreamDao: DreamDao) {

    val allDreams: Flow<List<Dream>> = dreamDao.getAllDreams()

    fun searchDreams(query: String): Flow<List<Dream>> {
        return dreamDao.searchDreams(query)
    }

    suspend fun getLeastRecentlyShownDream(): Dream? {
        return dreamDao.getLeastRecentlyShownDream()
    }

    suspend fun markDreamAsShown(dreamId: Int) {
        dreamDao.getDreamById(dreamId)?.let { dream ->
            val updated = dream.copy(lastShownMillis = System.currentTimeMillis())
            dreamDao.updateDream(updated)
        }
    }

    suspend fun insert(dream: Dream) {
        dreamDao.insertDream(dream)
    }

    suspend fun delete(dream: Dream) {
        dreamDao.deleteDream(dream)
    }
}
