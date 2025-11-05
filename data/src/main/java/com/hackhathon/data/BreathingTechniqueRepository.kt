package com.hackhathon.data

import com.hackhathon.data.models.BreathingTechniquesData
import com.hackhathon.data.utils.toDBO
import com.hackhathon.data.utils.toData
import com.hackhathon.local_database.RoomDatabase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BreathingTechniqueRepository @Inject constructor(
    private val roomDatabase: RoomDatabase,
) {

    fun observeFavoriteTechnique() : Flow<List<BreathingTechniquesData>> {
        return roomDatabase.roomDao.observeFavoriteTechnique().map { it.map { it.toData() } }
    }

    suspend fun setIsFavoriteTechnique(data : BreathingTechniquesData){
        roomDatabase.roomDao.insertFavoriteTechnique(data.toDBO())
    }

    suspend fun deleteIsFavoriteTechnique(data : BreathingTechniquesData){
        roomDatabase.roomDao.deleteFavoriteTechnique(data.id)
    }
}