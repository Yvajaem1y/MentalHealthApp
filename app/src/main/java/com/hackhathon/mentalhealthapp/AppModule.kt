package com.hackhathon.mentalhealthapp

import android.content.Context
import com.hackhathon.data.BreathingTechniqueRepository
import com.hackhathon.local_database.RoomDatabase
import com.hackhathon.yandex_gpt_api.YandexGPTClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYandexGPTClient(): YandexGPTClient {
        return YandexGPTClient(
            apiKey = BuildConfig.API_KEY,
            folderId = BuildConfig.FOLDER_ID

        )
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): RoomDatabase {
        return RoomDatabase(
            context
        )
    }

    @Provides
    @Singleton
    fun provideBreathingTechniqueRepository(roomDatabase: RoomDatabase): BreathingTechniqueRepository {
        return BreathingTechniqueRepository(roomDatabase)
    }

}