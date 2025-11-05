package com.hackhathon.data

import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.UserData
import com.hackhathon.data.utils.toData
import com.hackhathon.data.utils.toDbo
import com.hackhathon.local_database.RoomDatabase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserDataRepository @Inject constructor(
    private val roomDatabase: RoomDatabase,
) {
    fun observeUserData(): Flow<RequestResult<UserData>> {
        return flow {
            emit(RequestResult.InProgress())

            try {
                val initialUserData = roomDatabase.roomDao.observeUserData()
                    .firstOrNull()
                    ?.firstOrNull()
                    ?.toData() ?: UserData()

                emit(RequestResult.Success(initialUserData))
            } catch (e: Exception) {
                emit(RequestResult.Error<UserData>(error = e))
                return@flow
            }

            roomDatabase.roomDao.observeUserData()
                .map { dataDBOS ->
                    dataDBOS.firstOrNull()?.toData() ?: UserData()
                }
                .collect { userData ->
                    emit(RequestResult.Success(userData))
                }
        }
            .catch { error ->
                emit(RequestResult.Error<UserData>(error = error))
            }
    }

    suspend fun insertUserData(userData: UserData){
        roomDatabase.roomDao.insertUserData(userData.toDbo())
    }
}