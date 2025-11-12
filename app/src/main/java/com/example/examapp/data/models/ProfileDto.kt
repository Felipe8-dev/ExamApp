package com.example.examapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para el perfil de usuario en Supabase
 * Mapea la tabla 'profiles'
 */
@Serializable
data class ProfileDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("full_name")
    val fullName: String,
    
    @SerialName("role")
    val role: String, // "profesor" o "estudiante"
    
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
)

/**
 * DTO para insertar/actualizar perfil
 */
@Serializable
data class ProfileInsertDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("full_name")
    val fullName: String,
    
    @SerialName("role")
    val role: String
)

/**
 * DTO para actualizar perfil
 */
@Serializable
data class ProfileUpdateDto(
    @SerialName("full_name")
    val fullName: String? = null,
    
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

