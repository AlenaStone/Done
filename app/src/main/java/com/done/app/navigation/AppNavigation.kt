package com.done.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.done.app.ui.course.CourseScreen
import com.done.app.ui.home.HomeScreen
import com.done.app.ui.statistics.StatisticsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen()
        }

        composable("course") {
            CourseScreen()
        }

        composable("statistics") {
            StatisticsScreen()
        }
    }
}