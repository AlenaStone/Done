package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.done.app.data.model.Assignment
import com.done.app.data.repository.AssignmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class AssignmentViewModel(
    private val repository: AssignmentRepository,
    private val courseId: Int
) : ViewModel() {

    val assignments =
        repository.getAssignmentForCourse(courseId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addAssignment(
        assignmentName: String,
        date: LocalDate
    ) {
        viewModelScope.launch {

            repository.insertAssignment(
                Assignment(
                    courseId = courseId,
                    title = assignmentName,
                    date = date
                )
            )
        }
    }

    fun updateAssignment(
        assignment: Assignment
    ) {
        viewModelScope.launch {
            repository.updateAssignment(assignment)
        }
    }

    fun deleteAssignment(
        assignment: Assignment
    ) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
        }
    }
}
