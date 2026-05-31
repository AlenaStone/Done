package com.done.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.done.app.data.model.Exam
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Insert
    suspend fun insertExam(
        exam: Exam
    )

    @Update
    suspend fun updateExam(
        exam: Exam
    )
    @Delete
    suspend fun deleteExam(
        exam: Exam
    )

    @Query(
        "SELECT * FROM Exam WHERE courseId = :courseId"
    )
    fun getExamsForCourse(
        courseId: Int
    ): Flow<List<Exam>>

    @Query("SELECT * FROM Exam")
    fun getAllExams(): Flow<List<Exam>>
}
