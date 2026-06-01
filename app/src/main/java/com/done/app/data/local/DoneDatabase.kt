package com.done.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.done.app.data.model.Assignment
import com.done.app.data.model.Course
import com.done.app.data.model.Exam
import com.done.app.data.model.Task
@TypeConverters(
    Converters::class
)
@Database(
    entities = [
        Course::class,
        Task::class,
        Exam::class,
        Assignment::class
    ],
    version = 5,
    exportSchema = false
)
abstract class DoneDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun taskDao(): TaskDao

    abstract fun examDao(): ExamDao

    abstract fun assignmentDao(): AssignmentDao
}
