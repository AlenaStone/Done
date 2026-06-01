package com.done.app.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.done.app.ui.common.toDisplayDate
import com.done.app.viewmodel.CourseViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.time.LocalDate

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
    val totalItems =
        courseTasks.size + courseAssignments.size + courseExams.size
    val completedItems =
        courseTasks.count { it.isDone } +
                courseAssignments.count { it.isDone } +
                courseExams.count { it.isDone }
    val progress =
        if (totalItems == 0) 0 else completedItems * 100 / totalItems
    val openItems = totalItems - completedItems
    val averageGrade =
        (courseAssignments.mapNotNull { it.note } + courseExams.mapNotNull { it.note })
            .takeIf { it.isNotEmpty() }
            ?.average()
    var showAttendanceDialog by remember {
        mutableStateOf(false)
    }
    var totalClassesText by remember(course?.id, course?.totalClasses) {
        mutableStateOf(course?.totalClasses?.takeIf { it > 0 }?.toString() ?: "")
    }
    var missedClassesText by remember(course?.id, course?.missedClasses) {
        mutableStateOf(course?.missedClasses?.takeIf { it > 0 }?.toString() ?: "")
    }
    val totalClasses = totalClassesText.toIntOrNull()
    val missedClasses = missedClassesText.toIntOrNull()
    val canSaveAttendance =
        totalClasses != null &&
                missedClasses != null &&
                totalClasses >= 0 &&
                missedClasses >= 0 &&
                missedClasses <= totalClasses

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Text(course?.name ?: "Course")
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            CourseSummaryPanel(
                progress = progress,
                openItems = openItems,
                averageGrade = averageGrade,
                absencePercent = course?.absencePercent()
            )

            Spacer(modifier = Modifier.height(10.dp))

            AttendanceCard(
                course = course,
                onEditClick = {
                    totalClassesText = course?.totalClasses?.takeIf { it > 0 }?.toString() ?: ""
                    missedClassesText = course?.missedClasses?.takeIf { it > 0 }?.toString() ?: ""
                    showAttendanceDialog = true
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

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

        if (showAttendanceDialog && course != null) {
            AlertDialog(
                onDismissRequest = {
                    showAttendanceDialog = false
                },
                title = {
                    Text("Edit Attendance")
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = totalClassesText,
                            onValueChange = {
                                totalClassesText = it.filter(Char::isDigit)
                            },
                            label = {
                                Text("Total classes")
                            },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = missedClassesText,
                            onValueChange = {
                                missedClassesText = it.filter(Char::isDigit)
                            },
                            label = {
                                Text("Missed classes")
                            },
                            singleLine = true,
                            isError = missedClasses != null &&
                                    totalClasses != null &&
                                    missedClasses > totalClasses
                        )
                    }
                },
                confirmButton = {
                    Button(
                        enabled = canSaveAttendance,
                        onClick = {
                            if (totalClasses != null && missedClasses != null) {
                                viewModel.updateCourse(
                                    course.copy(
                                        totalClasses = totalClasses,
                                        missedClasses = missedClasses
                                    )
                                )
                                showAttendanceDialog = false
                            }
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAttendanceDialog = false
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
private fun CourseSummaryPanel(
    progress: Int,
    openItems: Int,
    averageGrade: Double?,
    absencePercent: Int?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryMetric(
                label = "Progress",
                value = "$progress%"
            )
            SummaryMetric(
                label = "Open",
                value = openItems.toString()
            )
            SummaryMetric(
                label = "Average",
                value = averageGrade?.let { "%.2f".format(it) } ?: "-"
            )
            SummaryMetric(
                label = "Absence",
                value = absencePercent?.let { "$it%" } ?: "-"
            )
        }
    }
}

@Composable
private fun AttendanceCard(
    course: com.done.app.data.model.Course?,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Attendance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = course?.attendanceText() ?: "No attendance data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit attendance",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SummaryMetric(
    label: String,
    value: String
) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun com.done.app.data.model.Course.absencePercent(): Int? {
    if (totalClasses <= 0) {
        return null
    }

    return missedClasses * 100 / totalClasses
}

private fun com.done.app.data.model.Course.attendanceText(): String {
    val percent = absencePercent()
        ?: return "No attendance data"

    return "$missedClasses/$totalClasses missed | $percent%"
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
        return "Next: ${nextAssignment.date.toDisplayDate()}"
    }

    return if (grades.isEmpty()) {
        "$completedCount/${size} completed"
    } else {
        "$completedCount/${size} completed | Avg grade: %.2f".format(grades.average())
    }
}

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
        return "Next: ${nextExam.date.toDisplayDate()}"
    }

    return if (grades.isEmpty()) {
        "$completedCount/${size} completed"
    } else {
        "$completedCount/${size} completed | Avg grade: %.2f".format(grades.average())
    }
}
