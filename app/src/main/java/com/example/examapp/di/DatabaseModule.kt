package com.example.examapp.di

import android.content.Context
import androidx.room.Room
import com.example.examapp.data.local.ExamDatabase
import com.example.examapp.data.network.SupabaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseConfig.client
    }
    
    @Provides
    @Singleton
    fun provideExamDatabase(@ApplicationContext context: Context): ExamDatabase {
        return Room.databaseBuilder(
            context,
            ExamDatabase::class.java,
            ExamDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideQuestionDao(database: ExamDatabase) = database.questionDao()
    
    @Provides
    fun provideExamHistoryDao(database: ExamDatabase) = database.examHistoryDao()
    
    @Provides
    fun provideQuestionAttemptDao(database: ExamDatabase) = database.questionAttemptDao()
}