package com.done.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.done.app.data.repository.AssignmentRepository
import com.done.app.data.repository.ExamRepository
import com.done.app.data.repository.TaskRepository
import com.done.app.data.repository.TipsRepository
import com.done.app.ui.course.AssignmentsScreen
import com.done.app.ui.course.CourseScreen
import com.done.app.ui.course.ExamScreen
import com.done.app.ui.course.TasksScreen
import com.done.app.ui.home.HomeScreen
import com.done.app.ui.settings.SettingsScreen
import com.done.app.ui.settings.ThemeSetting
import com.done.app.ui.statistics.StatisticsScreen
import com.done.app.viewmodel.AssignmentViewModel
import com.done.app.viewmodel.AssignmentViewModelFactory
import com.done.app.viewmodel.CourseViewModel
import com.done.app.viewmodel.CourseViewModelFactory
import com.done.app.viewmodel.ExamViewModel
import com.done.app.viewmodel.ExamViewModelFactory
import com.done.app.viewmodel.TaskViewModel
import com.done.app.viewmodel.TaskViewModelFactory
import com.done.app.viewmodel.TipsViewModel
import com.done.app.viewmodel.TipsViewModelFactory

@Composable
fun AppNavigation(
    courseFactory: CourseViewModelFactory,
    taskRepository: TaskRepository,
    examRepository: ExamRepository,
    assignmentRepository: AssignmentRepository,
    tipsRepository: TipsRepository,
    themeSetting: ThemeSetting,
    onThemeChange: (ThemeSetting) -> Unit,
    onClearData: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            val courseViewModel: CourseViewModel =
                viewModel(
                    factory = courseFactory
                )
            val tipsViewModel: TipsViewModel =
                viewModel(
                    factory = TipsViewModelFactory(
                        repository = tipsRepository
                    )
                )

            HomeScreen(
                navController = navController,
                viewModel = courseViewModel,
                tipsViewModel = tipsViewModel
            )
        }

        composable("course/{courseId}") { backStackEntry ->

            val courseId =
                backStackEntry.arguments
                    ?.getString("courseId")
                    ?.toInt()
                    ?: 0

            val courseViewModel: CourseViewModel =
                viewModel(
                    factory = courseFactory
                )

            CourseScreen(
                courseId = courseId,
                navController = navController,
                viewModel = courseViewModel
            )
        }

        composable("tasks/{courseId}") { backStackEntry ->

            val courseId =
                backStackEntry.arguments
                    ?.getString("courseId")
                    ?.toInt()
                    ?: 0
            val taskFactory =
                TaskViewModelFactory(
                    repository = taskRepository,
                    courseId = courseId
                )
            val taskViewModel: TaskViewModel =
                viewModel(
                    factory = taskFactory
                )
            TasksScreen(
                navController = navController,
                viewModel = taskViewModel
            )
        }

        composable("assignments/{courseId}") { backStackEntry ->

            val courseId =
                backStackEntry.arguments
                    ?.getString("courseId")
                    ?.toInt()
                    ?: 0
            val assignmentFactory =
                AssignmentViewModelFactory(
                    repository = assignmentRepository,
                    courseId = courseId
                )
            val assignmentViewModel: AssignmentViewModel =
                viewModel(
                    factory = assignmentFactory
                )
            AssignmentsScreen(
                navController = navController,
                viewModel = assignmentViewModel
            )
        }

        composable("exams/{courseId}") { backStackEntry ->

            val courseId =
                backStackEntry.arguments
                    ?.getString("courseId")
                    ?.toInt()
                    ?: 0
            val examFactory =
                ExamViewModelFactory(
                    repository = examRepository,
                    courseId = courseId
                )
            val examViewModel: ExamViewModel =
                viewModel(
                    factory = examFactory
                )
            ExamScreen(
                navController = navController,
                viewModel = examViewModel
            )
        }

        composable("statistics") {
            StatisticsScreen()
        }

        composable("settings") {
            SettingsScreen(
                navController = navController,
                themeSetting = themeSetting,
                onThemeChange = onThemeChange,
                onClearData = onClearData
            )
        }
    }
}

