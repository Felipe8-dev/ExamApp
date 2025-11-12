package com.example.examapp.data.mappers

import com.example.examapp.data.models.ProfileDto
import com.example.examapp.data.models.ProfileInsertDto
import com.example.examapp.domain.entities.User
import com.example.examapp.domain.entities.UserType

/**
 * Mapper para convertir entre ProfileDto y User (entidad de dominio)
 */
object ProfileMapper {
    
    /**
     * Convierte ProfileDto a User
     */
    fun ProfileDto.toDomain(): User {
        return User(
            id = id,
            email = email,
            fullName = fullName,
            userType = when (role.lowercase()) {
                "profesor" -> UserType.PROFESSOR
                "estudiante" -> UserType.STUDENT
                else -> UserType.STUDENT // Por defecto
            },
            avatarUrl = avatarUrl
        )
    }
    
    /**
     * Convierte User a ProfileInsertDto
     */
    fun User.toInsertDto(): ProfileInsertDto {
        return ProfileInsertDto(
            id = id,
            email = email,
            fullName = fullName,
            role = when (userType) {
                UserType.PROFESSOR -> "profesor"
                UserType.STUDENT -> "estudiante"
            }
        )
    }
    
    /**
     * Convierte lista de ProfileDto a lista de User
     */
    fun List<ProfileDto>.toDomain(): List<User> {
        return map { it.toDomain() }
    }
}

