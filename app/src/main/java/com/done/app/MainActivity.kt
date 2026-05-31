package com.done.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.done.app.data.local.DatabaseProvider
import com.done.app.data.repository.CourseRepository
import com.done.app.data.repository.TaskRepository
import com.done.app.data.repository.ExamRepository
import com.done.app.data.repository.AssignmentRepository
import com.done.app.navigation.AppNavigation
import com.done.app.ui.theme.DoneTheme
import com.done.app.viewmodel.CourseViewModelFactory

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val db = DatabaseProvider.getDatabase(
            applicationContext
        )

        val courseDao = db.courseDao()

        val repository =
            CourseRepository(courseDao)

        val taskRepository =
            TaskRepository(
                db.taskDao()
            )
        val examRepository =
            ExamRepository(
                db.examDao()
            )
        val assignmentRepository =
            AssignmentRepository(
                db.assignmentDao()
            )

        val factory =
            CourseViewModelFactory(
                repository = repository,
                taskRepository = taskRepository,
                examRepository = examRepository,
                assignmentRepository = assignmentRepository
            )
        setContent {
            DoneTheme {
                AppNavigation(
                    courseFactory = factory,
                    taskRepository = taskRepository,
                    examRepository = examRepository,
                    assignmentRepository = assignmentRepository
                )
            }
        }
    }
}
