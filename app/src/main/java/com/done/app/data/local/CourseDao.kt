package com.done.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.done.app.data.model.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert
    suspend fun insertCourse(
        course: Course
    )

    @Delete
    suspend fun deleteCourse(
        course: Course
    )

    @Query("SELECT * FROM Course")
    fun getAllCourses(): Flow<List<Course>>
}