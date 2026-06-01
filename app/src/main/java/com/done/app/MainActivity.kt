package com.done.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.done.app.data.local.DatabaseProvider
import com.done.app.data.repository.CourseRepository
import com.done.app.data.repository.TaskRepository
import com.done.app.data.repository.ExamRepository
import com.done.app.data.repository.AssignmentRepository
import com.done.app.data.repository.TipsRepository
import com.done.app.navigation.AppNavigation
import com.done.app.ui.settings.ThemeSetting
import com.done.app.ui.theme.DoneTheme
import com.done.app.viewmodel.CourseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {

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
        val tipsRepository =
            TipsRepository()

        val factory =
            CourseViewModelFactory(
                repository = repository,
                taskRepository = taskRepository,
                examRepository = examRepository,
                assignmentRepository = assignmentRepository
            )
        setContent {
            val preferences = remember {
                getSharedPreferences("done_settings", MODE_PRIVATE)
            }
            var themeSetting by remember {
                mutableStateOf(
                    preferences.getString("theme", ThemeSetting.System.name)
                        ?.let(ThemeSetting::valueOf)
                        ?: ThemeSetting.System
                )
            }
            val systemDarkTheme = isSystemInDarkTheme()
            val darkTheme =
                when (themeSetting) {
                    ThemeSetting.System -> systemDarkTheme
                    ThemeSetting.Light -> false
                    ThemeSetting.Dark -> true
                }

            DoneTheme(
                darkTheme = darkTheme
            ) {
                AppNavigation(
                    courseFactory = factory,
                    taskRepository = taskRepository,
                    examRepository = examRepository,
                    assignmentRepository = assignmentRepository,
                    tipsRepository = tipsRepository,
                    themeSetting = themeSetting,
                    onThemeChange = { nextTheme ->
                        themeSetting = nextTheme
                        preferences.edit()
                            .putString("theme", nextTheme.name)
                            .apply()
                    },
                    onClearData = {
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.clearAllTables()
                        }
                    }
                )
            }
        }
    }
}
