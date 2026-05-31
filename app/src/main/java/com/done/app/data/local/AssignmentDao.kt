package com.done.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.done.app.data.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {

    @Insert
    suspend fun insertAssignment(
        assignment: Assignment
    )

    @Update
    suspend fun updateAssignment(
        assignment: Assignment
    )
    @Delete
    suspend fun deleteAssignment(
       assignment: Assignment
    )

    @Query(
        "SELECT * FROM Assignment WHERE courseId = :courseId"
    )
    fun getAssignmentForCourse(
        courseId: Int
    ): Flow<List<Assignment>>

    @Query("SELECT * FROM Assignment")
    fun getAllAssignments(): Flow<List<Assignment>>
}
