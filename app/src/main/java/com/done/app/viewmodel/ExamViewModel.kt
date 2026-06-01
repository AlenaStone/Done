package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.done.app.data.model.Exam
import com.done.app.data.repository.ExamRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExamViewModel(
    private val repository: ExamRepository,
    private val courseId: Int
) : ViewModel() {

    val exams =
        repository.getExamsForCourse(courseId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addExam(
        examName: String,
        date: LocalDate
    ) {
        viewModelScope.launch {

            repository.insertExam(
                Exam(
                    courseId = courseId,
                    title = examName,
                    date = date
                )
            )
        }
    }

    fun updateExam(
        exam: Exam
    ) {
        viewModelScope.launch {
            repository.updateExam(exam)
        }
    }

    fun deleteExam(
        exam: Exam
    ) {
        viewModelScope.launch {
            repository.deleteExam(exam)
        }
    }
}
