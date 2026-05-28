package com.done.app.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.done.app.data.model.Assignment
import com.done.app.data.model.Course
import com.done.app.data.model.Exam
import com.done.app.data.model.Task

object AppRepository {

    val courses = mutableStateListOf<Course>()

    val tasks = mutableStateListOf<Task>()

    val assignments = mutableStateListOf<Assignment>()

    val exams = mutableStateListOf<Exam>()
}