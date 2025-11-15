package com.example.examapp

import android.app.Application
import com.example.examapp.data.local.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExamApplication : Application() {
    
    @Inject
    lateinit var databaseInitializer: DatabaseInitializer
    
    override fun onCreate() {
        super.onCreate()
        // Inicializar la base de datos con preguntas si es necesario
        databaseInitializer.initializeIfNeeded()
    }
}