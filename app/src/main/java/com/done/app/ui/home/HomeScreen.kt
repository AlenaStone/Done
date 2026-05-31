package com.done.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.done.app.data.model.Assignment
import com.done.app.data.model.Course
import com.done.app.data.model.Exam
import com.done.app.data.model.Task
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CourseViewModel
) {
    var newCourseName by remember {
        mutableStateOf("")
    }
    var showAddCourseDialog by remember {
        mutableStateOf(false)
    }
    val newCourseNameTrimmed = newCourseName.trim()
    val courses by viewModel.courses.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val exams by viewModel.exams.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text("Done")
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFD5D0F0)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddCourseDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add course"
                )
            }
        }
    ) { paddingValues ->
        if (showAddCourseDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddCourseDialog = false
                },
                title = {
                    Text("Add Course")
                },
                text = {
                    OutlinedTextField(
                        value = newCourseName,
                        onValueChange = {
                            newCourseName = it
                        },
                        label = {
                            Text("Course Name")
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        enabled = newCourseNameTrimmed.isNotEmpty(),
                        onClick = {
                            if (newCourseNameTrimmed.isNotEmpty()) {
                                viewModel.addCourse(
                                    newCourseNameTrimmed
                                )

                                newCourseName = ""
                                showAddCourseDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddCourseDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7FC))
            .padding(paddingValues)
        ) {
            if (courses.isEmpty()) {
                EmptyState(
                    text = "No courses yet. Tap + to add one."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(courses) { course ->
                        val summary = course.summary(
                            tasks = tasks,
                            assignments = assignments,
                            exams = exams
                        )

                        CourseCard(
                            course = course,
                            progress = summary.progress,
                            averageGrade = summary.averageGrade,
                            onDeleteClick = {
                                viewModel.deleteCourse(course)
                            },
                            onClick = {
                                navController.navigate(
                                    "course/${course.id}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

private data class CourseSummary(
    val progress: Int,
    val averageGrade: Double?
)

private fun Course.summary(
    tasks: List<Task>,
    assignments: List<Assignment>,
    exams: List<Exam>
): CourseSummary {
    val courseTasks = tasks.filter { it.courseId == id }
    val courseAssignments = assignments.filter { it.courseId == id }
    val courseExams = exams.filter { it.courseId == id }

    val totalItems =
        courseTasks.size +
                courseAssignments.size +
                courseExams.size

    val completedItems =
        courseTasks.count { it.isDone } +
                courseAssignments.count { it.isDone } +
                courseExams.count { it.isDone }

    val grades =
        courseAssignments.mapNotNull { it.note } +
                courseExams.mapNotNull { it.note }

    return CourseSummary(
        progress = if (totalItems == 0) 0 else completedItems * 100 / totalItems,
        averageGrade = grades.takeIf { it.isNotEmpty() }?.average()
    )
}
