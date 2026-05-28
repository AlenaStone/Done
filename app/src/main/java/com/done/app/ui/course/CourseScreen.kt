package com.done.app.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    courseName: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text(courseName)
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFD5D0F0)
                )
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            Spacer(modifier = Modifier.height(24.dp))

            CategoryCard(
                title = "Tasks",
                onClick = {
                    navController.navigate(
                        "tasks/$courseName"
                    )
                }
            )

            CategoryCard(
                title = "Assignments",
                onClick = {
                    navController.navigate(
                        "assignments/$courseName"
                    )
                }
            )

            CategoryCard(
                title = "Exams",
                onClick = {
                    navController.navigate(
                        "exams/$courseName"
                    )
                }
            )
        }
    }
}
