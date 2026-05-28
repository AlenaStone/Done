package com.done.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.done.app.data.model.Course
import com.done.app.data.repository.AppRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var newCourseName by remember {
        mutableStateOf("")
    }
    var nextId by remember {
        mutableIntStateOf(4)
    }
    var showAddCourseDialog by remember {
        mutableStateOf(false)
    }
    val newCourseNameTrimmed = newCourseName.trim()

    val tasks = AppRepository.tasks
    val assignments = AppRepository.assignments
    val exams = AppRepository.exams

    val courses = remember {
        mutableStateListOf(
            Course(
                id = 1,
                name = "Mobile Development",
                progress = 80
            ),
            Course(
                id = 2,
                name = "Cloud Computing",
                progress = 20
            ),
            Course(
                id = 3,
                name = "Web Engineering",
                progress = 50
            )
        )
    }

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
                                courses.add(
                                    Course(
                                        id = nextId,
                                        name = newCourseNameTrimmed,
                                        progress = 0
                                    )
                                )

                                nextId++
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No courses yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6D6A7A)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(courses) { course ->
                        val courseTasks =
                            tasks.filter {
                                it.courseId == course.id
                            }

                        val courseAssignments =
                            assignments.filter {
                                it.courseId == course.id
                            }

                        val courseExams =
                            exams.filter {
                                it.courseId == course.id
                            }

                        val totalItems =
                            courseTasks.size +
                                    courseAssignments.size +
                                    courseExams.size

                        val completedItems =
                            courseTasks.count { it.isDone } +
                                    courseAssignments.count { it.isDone } +
                                    courseExams.count { it.isDone }

                        val progress =
                            if (totalItems == 0)
                                0
                            else
                                completedItems * 100 / totalItems

                        val grades =
                            courseAssignments.mapNotNull { it.note } +
                                    courseExams.mapNotNull { it.note }

                        val averageGrade =
                            if (grades.isEmpty())
                                null
                            else
                                grades.average()
                        CourseCard(
                            course = course,
                            progress = progress,
                            averageGrade = averageGrade,
                            onDeleteClick = {
                                courses.remove(course)
                            },
                            onClick = {
                                navController.navigate(
                                    "course/${course.name}"
                                )

                            }
                        )
                    }
                }
            }
        }
    }
}
