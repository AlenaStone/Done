package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.done.app.data.model.Task
import com.done.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    private val repository: TaskRepository,
    private val courseId: Int
) : ViewModel() {

    val tasks =
        repository.getTasksForCourse(courseId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addTask(
        taskName: String,
        deadline: LocalDate
    ) {
        viewModelScope.launch {

            repository.insertTask(
                Task(
                    courseId = courseId,
                    name = taskName,
                    deadline = deadline
                )
            )
        }
    }

    fun updateTask(
        task: Task
    ) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(
        task: Task
    ) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
