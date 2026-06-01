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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.done.app.data.model.Assignment
import com.done.app.data.model.Course
import com.done.app.data.model.Exam
import com.done.app.data.model.Task
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.CourseViewModel
import com.done.app.viewmodel.TipsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CourseViewModel,
    tipsViewModel: TipsViewModel
) {
    var newCourseName by remember {
        mutableStateOf("")
    }
    var showAddCourseDialog by remember {
        mutableStateOf(false)
    }
    var selectedTab by remember {
        mutableStateOf(HomeTab.Courses)
    }
    var coursePendingDelete by remember {
        mutableStateOf<Course?>(null)
    }
    val newCourseNameTrimmed = newCourseName.trim()
    val courses by viewModel.courses.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val tipsState by tipsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Done",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = if (selectedTab == HomeTab.Courses)
                                    "Your study dashboard"
                                else
                                    "Study tips",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate("settings")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == HomeTab.Courses) {
                FloatingActionButton(
                    onClick = {
                        showAddCourseDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add course"
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Courses,
                    onClick = {
                        selectedTab = HomeTab.Courses
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Study")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == HomeTab.Tips,
                    onClick = {
                        selectedTab = HomeTab.Tips
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Tips")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f)
                    )
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
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                HomeTab.Courses -> CoursesTab(
                    courses = courses,
                    tasks = tasks,
                    assignments = assignments,
                    exams = exams,
                    onCourseClick = { course ->
                        navController.navigate(
                            "course/${course.id}"
                        )
                    },
                    onDeleteCourse = { course ->
                        coursePendingDelete = course
                    }
                )

                HomeTab.Tips -> TipsTab(
                    state = tipsState,
                    courses = courses,
                    tasks = tasks,
                    assignments = assignments,
                    exams = exams,
                    onRefresh = {
                        tipsViewModel.refreshTip()
                    }
                )
            }
        }

        coursePendingDelete?.let { course ->
            AlertDialog(
                onDismissRequest = {
                    coursePendingDelete = null
                },
                title = {
                    Text("Delete course?")
                },
                text = {
                    Text("This course will be removed from your dashboard.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteCourse(course)
                            coursePendingDelete = null
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            coursePendingDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun CoursesTab(
    courses: List<Course>,
    tasks: List<Task>,
    assignments: List<Assignment>,
    exams: List<Exam>,
    onCourseClick: (Course) -> Unit,
    onDeleteCourse: (Course) -> Unit
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
            items(
                items = courses,
                key = { course -> course.id }
            ) { course ->
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
                        onDeleteCourse(course)
                    },
                    onClick = {
                        onCourseClick(course)
                    }
                )
            }
        }
    }
}

private enum class HomeTab {
    Courses,
    Tips
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
