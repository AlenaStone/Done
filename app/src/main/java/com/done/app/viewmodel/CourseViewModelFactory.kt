package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.done.app.data.repository.AssignmentRepository
import com.done.app.data.repository.CourseRepository
import com.done.app.data.repository.ExamRepository
import com.done.app.data.repository.TaskRepository

class CourseViewModelFactory(
    private val repository: CourseRepository,
    private val taskRepository: TaskRepository,
    private val examRepository: ExamRepository,
    private val assignmentRepository: AssignmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(
                repository = repository,
                taskRepository = taskRepository,
                examRepository = examRepository,
                assignmentRepository = assignmentRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
