package com.done.app.ui.course

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.done.app.viewmodel.CourseViewModel
import androidx.compose.runtime.getValue
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    courseId: Int,
    navController: NavController,
    viewModel: CourseViewModel
){

    val courses by viewModel.courses.collectAsState()

    val course =
        courses.find {
            it.id == courseId
        }
    val tasks by viewModel.tasks.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val exams by viewModel.exams.collectAsState()

    val courseTasks = tasks.filter { it.courseId == courseId }
    val courseAssignments = assignments.filter { it.courseId == courseId }
    val courseExams = exams.filter { it.courseId == courseId }

    val taskSummary = courseTasks.itemSummary(
        completedCount = courseTasks.count { it.isDone }
    )
    val assignmentSummary = courseAssignments.assignmentSummary()
    val examSummary = courseExams.examSummary()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text(course?.name ?: "Course")
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
                .background(Color(0xFFF7F7FC))
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            CategoryCard(
                title = "Tasks",
                summary = taskSummary,
                count = courseTasks.size,
                icon = Icons.Default.CheckCircle,
                accentColor = Color(0xFF2F80ED),
                onClick = {
                    navController.navigate(
                        "tasks/$courseId"
                    )
                }
            )

            CategoryCard(
                title = "Assignments",
                summary = assignmentSummary,
                count = courseAssignments.size,
                icon = Icons.Default.Add,
                accentColor = Color(0xFF7B61FF),
                onClick = {
                    navController.navigate(
                        "assignments/$courseId"
                    )
                }
            )

            CategoryCard(
                title = "Exams",
                summary = examSummary,
                count = courseExams.size,
                icon = Icons.Default.DateRange,
                accentColor = Color(0xFFE05A47),
                onClick = {
                    navController.navigate(
                        "exams/$courseId"
                    )
                }
            )
        }
    }
}

private fun <T> List<T>.itemSummary(
    completedCount: Int
): String {
    return if (isEmpty()) {
        "Nothing added yet"
    } else {
        "$completedCount/${size} completed"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun List<com.done.app.data.model.Assignment>.assignmentSummary(): String {
    if (isEmpty()) {
        return "Nothing added yet"
    }

    val grades = mapNotNull { it.note }
    val completedCount = count { it.isDone }
    val nextAssignment =
        filter { !it.isDone && !it.date.isBefore(LocalDate.now()) }
            .minByOrNull { it.date }

    if (completedCount < size && nextAssignment != null) {
        return "Next: ${nextAssignment.date}"
    }

    return if (grades.isEmpty()) {
        "$completedCount/${size} completed"
    } else {
        "$completedCount/${size} completed · Avg grade: %.2f".format(grades.average())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun List<com.done.app.data.model.Exam>.examSummary(): String {
    if (isEmpty()) {
        return "Nothing added yet"
    }

    val grades = mapNotNull { it.note }
    val completedCount = count { it.isDone }
    val nextExam =
        filter { !it.isDone && !it.date.isBefore(LocalDate.now()) }
            .minByOrNull { it.date }

    if (completedCount < size && nextExam != null) {
        return "Next: ${nextExam.date}"
    }

    return if (grades.isEmpty()) {
        "$completedCount/${size} completed"
    } else {
        "$completedCount/${size} completed · Avg grade: %.2f".format(grades.average())
    }
}
