package com.example.examapp.data.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.android.Android

/**
 * Configuración del cliente de Supabase
 * 
 * IMPORTANTE: Antes de usar, debes:
 * 1. Crear un proyecto en https://supabase.com
 * 2. Obtener tu SUPABASE_URL y SUPABASE_ANON_KEY del dashboard
 * 3. Configurar OAuth providers (Google, GitHub, Facebook) en Authentication > Providers
 * 4. Ejecutar los scripts SQL de la carpeta /supabase en tu base de datos
 * 5. Configurar las URLs de redirección en Authentication > URL Configuration
 */
object SupabaseConfig {
    
    // TODO: Reemplaza estas credenciales con las de tu proyecto
    private const val SUPABASE_URL = "https://foulfpimejnwhktayjrn.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZvdWxmcGltZWpud2hrdGF5anJuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgxNjUxMzcsImV4cCI6MjA3Mzc0MTEzN30.sdaQA4Wklb3bCQN-4IzbXqcj4TqtZ9EaEcRx5P5qXic"
    
    /**
     * Cliente singleton de Supabase
     */
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            // Configurar motor de red Android
            httpEngine = Android.create()
            
            // Módulo de autenticación
            install(Auth) {
                // Esquema de deep linking para OAuth
                scheme = "examapp"
                host = "auth-callback"
            }
            
            // Módulo de base de datos (PostgREST)
            install(Postgrest)
            
            // Módulo de tiempo real (opcional, para actualizaciones en vivo)
            install(Realtime)
        }
    }
    
    /**
     * Acceso rápido al módulo de autenticación
     */
    val auth: Auth
        get() = client.auth
    
    /**
     * Acceso rápido al módulo de base de datos
     */
    val postgrest: Postgrest
        get() = client.postgrest
    
    /**
     * Verifica si el usuario está autenticado
     */
    suspend fun isUserAuthenticated(): Boolean {
        return try {
            auth.currentSessionOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obtiene el ID del usuario actual
     */
    suspend fun getCurrentUserId(): String? {
        return try {
            auth.currentUserOrNull()?.id
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Obtiene el email del usuario actual
     */
    suspend fun getCurrentUserEmail(): String? {
        return try {
            auth.currentUserOrNull()?.email
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Extensión para facilitar el uso del cliente en otros lugares
 */
val supabase: SupabaseClient
    get() = SupabaseConfig.client