package com.example.examapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.data.local.entities.LocalExamHistory
import com.example.examapp.data.repositories.LocalExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ExamHistoryViewModel @Inject constructor(
    private val localExamRepository: LocalExamRepository
) : ViewModel() {
    
    fun getExamHistoryByUser(userId: String): Flow<List<LocalExamHistory>> {
        return localExamRepository.getExamHistoryByUser(userId)
    }
}

