package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.done.app.data.model.Course
import com.done.app.data.repository.AssignmentRepository
import com.done.app.data.repository.CourseRepository
import com.done.app.data.repository.ExamRepository
import com.done.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: CourseRepository,
    taskRepository: TaskRepository,
    examRepository: ExamRepository,
    assignmentRepository: AssignmentRepository
) : ViewModel() {

    val courses =
        repository.getAllCourses()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val tasks =
        taskRepository.getAllTasks()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val exams =
        examRepository.getAllExams()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val assignments =
        assignmentRepository.getAllAssignments()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addCourse(
        courseName: String
    ) {
        viewModelScope.launch {

            repository.insertCourse(
                Course(
                    name = courseName
                )
            )
        }
    }

    fun deleteCourse(
        course: Course
    ) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
}
