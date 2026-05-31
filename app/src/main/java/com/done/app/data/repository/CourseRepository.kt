package com.done.app.data.repository

import com.done.app.data.local.CourseDao
import com.done.app.data.model.Course
import kotlinx.coroutines.flow.Flow

class CourseRepository(
    private val courseDao: CourseDao
) {

    fun getAllCourses(): Flow<List<Course>> {
        return courseDao.getAllCourses()
    }

    suspend fun insertCourse(
        course: Course
    ) {
        courseDao.insertCourse(course)
    }

    suspend fun deleteCourse(
        course: Course
    ) {
        courseDao.deleteCourse(course)
    }
}