package com.done.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.done.app.ui.course.AssignmentsScreen
import com.done.app.ui.course.CourseScreen
import com.done.app.ui.course.ExamScreen
import com.done.app.ui.course.TasksScreen
import com.done.app.ui.home.HomeScreen
import com.done.app.ui.statistics.StatisticsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(navController)
        }

        composable("course/{courseName}") { backStackEntry ->

            val courseName =
                backStackEntry.arguments
                    ?.getString("courseName")
                    ?: "Unknown Course"

            CourseScreen(
                courseName = courseName,
                navController = navController
            )
        }

        composable("tasks/{courseName}") { backStackEntry ->

            val courseName =
                backStackEntry.arguments
                    ?.getString("courseName")
                    ?: "Unknown Course"

            TasksScreen(
                courseName = courseName
            )
        }

        composable("assignments/{courseName}") { backStackEntry ->

            val courseName =
                backStackEntry.arguments
                    ?.getString("courseName")
                    ?: "Unknown Course"

            AssignmentsScreen(
                courseName = courseName
            )
        }

        composable("exams/{courseName}") { backStackEntry ->

            val courseName =
                backStackEntry.arguments
                    ?.getString("courseName")
                    ?: "Unknown Course"

            ExamScreen(
                courseName = courseName
            )
        }

        composable("statistics") {
            StatisticsScreen()
        }
    }
}
