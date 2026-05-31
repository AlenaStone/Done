package com.done.app.data.repository

import com.done.app.data.local.TaskDao
import com.done.app.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun getTasksForCourse(courseId: Int): Flow<List<Task>> {
        return taskDao.getTasksForCourse(courseId)
    }

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    suspend fun insertTask(
        task: Task
    ) {
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(
        task: Task
    ) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(
        task: Task
    ) {
        taskDao.updateTask(task)
    }
}
