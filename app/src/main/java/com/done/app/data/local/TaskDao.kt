package com.done.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.done.app.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(
        task: Task
    )

    @Update
    suspend fun updateTask(
        task: Task
    )
    @Delete
    suspend fun deleteTask(
        task: Task
    )

    @Query(
        "SELECT * FROM Task WHERE courseId = :courseId"
    )
    fun getTasksForCourse(
        courseId: Int
    ): Flow<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>
}
