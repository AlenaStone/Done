package com.done.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Assignment
import com.done.app.data.model.Course
import com.done.app.data.model.Exam
import com.done.app.data.model.Task
import com.done.app.ui.common.toDisplayDate
import com.done.app.viewmodel.TipsUiState
import java.time.LocalDate

@Composable
fun TipsTab(
    state: TipsUiState,
    courses: List<Course>,
    tasks: List<Task>,
    assignments: List<Assignment>,
    exams: List<Exam>,
    onRefresh: () -> Unit
) {
    val upcomingItems =
        remember(courses, tasks, assignments, exams) {
            buildUpcomingItems(
                courses = courses,
                tasks = tasks,
                assignments = assignments,
                exams = exams
            )
        }
    val ungradedItems =
        remember(courses, assignments, exams) {
            buildUngradedItems(
                courses = courses,
                assignments = assignments,
                exams = exams
            )
        }
    val attendanceWarnings =
        remember(courses) {
            courses
                .filter { course -> course.totalClasses > 0 }
                .sortedByDescending { course -> course.missedClasses * 100 / course.totalClasses }
                .take(3)
        }

    ShakeRefreshEffect(
        enabled = !state.isLoading,
        onShake = onRefresh
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DailyTipCard(
            state = state,
            onRefresh = onRefresh
        )

        UpcomingCard(
            items = upcomingItems
        )

        StudyToolsCard(
            upcomingItems = upcomingItems,
            ungradedItems = ungradedItems
        )

        AttendanceWarningsCard(
            courses = attendanceWarnings
        )
    }
}

@Composable
private fun DailyTipCard(
    state: TipsUiState,
    onRefresh: () -> Unit
) {
    InfoCard(
        title = "Daily Tip"
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null && state.tip == null -> {
                Text(
                    text = "Plan the next study block before you start. One clear task is easier to finish than a vague goal.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> {
                Text(
                    text = state.tip?.takeIf { it.isNotBlank() }
                        ?: "Start with the closest deadline, then finish one small task before opening the next one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRefresh,
            enabled = !state.isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
            Text(
                text = "Refresh",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun UpcomingCard(
    items: List<UpcomingItem>
) {
    InfoCard(
        title = "Upcoming"
    ) {
        if (items.isEmpty()) {
            Text(
                text = "No open deadlines yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            ResultList {
                items.forEachIndexed { index, item ->
                    ResultRow(
                        title = item.title,
                        meta = "${item.courseName} | ${item.type}",
                        trailing = item.date.toDisplayDate()
                    )
                    if (index < items.lastIndex) {
                        ResultDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceWarningsCard(
    courses: List<Course>
) {
    InfoCard(
        title = "Attendance"
    ) {
        if (courses.isEmpty()) {
            Text(
                text = "No attendance data yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            ResultList {
                courses.forEachIndexed { index, course ->
                    val percent = course.missedClasses * 100 / course.totalClasses
                    ResultRow(
                        title = course.name,
                        meta = "${course.missedClasses}/${course.totalClasses} missed",
                        trailing = "$percent%",
                        isWarning = percent >= 25
                    )
                    if (index < courses.lastIndex) {
                        ResultDivider()
                    }
                }
            }
        }
    }
}

@Composable
internal fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
internal fun ResultList(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
internal fun ResultRow(
    title: String,
    meta: String,
    trailing: String? = null,
    isWarning: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = meta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        trailing?.let { value ->
            Text(
                text = value,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isWarning)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
internal fun ResultDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    )
}

internal data class UpcomingItem(
    val title: String,
    val courseName: String,
    val type: String,
    val date: LocalDate
)

internal data class UngradedItem(
    val title: String,
    val courseName: String,
    val type: String
)

private fun buildUpcomingItems(
    courses: List<Course>,
    tasks: List<Task>,
    assignments: List<Assignment>,
    exams: List<Exam>
): List<UpcomingItem> {
    val courseNames =
        courses.associate { course ->
            course.id to course.name
        }

    val taskItems =
        tasks
            .filter { task -> !task.isDone && !task.deadline.isBefore(LocalDate.now()) }
            .map { task ->
                UpcomingItem(
                    title = task.name,
                    courseName = courseNames[task.courseId] ?: "Course",
                    type = "Task",
                    date = task.deadline
                )
            }
    val assignmentItems =
        assignments
            .filter { assignment -> !assignment.isDone && !assignment.date.isBefore(LocalDate.now()) }
            .map { assignment ->
                UpcomingItem(
                    title = assignment.title,
                    courseName = courseNames[assignment.courseId] ?: "Course",
                    type = "Assignment",
                    date = assignment.date
                )
            }
    val examItems =
        exams
            .filter { exam -> !exam.isDone && !exam.date.isBefore(LocalDate.now()) }
            .map { exam ->
                UpcomingItem(
                    title = exam.title,
                    courseName = courseNames[exam.courseId] ?: "Course",
                    type = "Exam",
                    date = exam.date
                )
            }

    return (taskItems + assignmentItems + examItems)
        .sortedBy { item -> item.date }
        .take(5)
}

private fun buildUngradedItems(
    courses: List<Course>,
    assignments: List<Assignment>,
    exams: List<Exam>
): List<UngradedItem> {
    val courseNames =
        courses.associate { course ->
            course.id to course.name
        }

    val assignmentItems =
        assignments
            .filter { assignment -> assignment.note == null }
            .map { assignment ->
                UngradedItem(
                    title = assignment.title,
                    courseName = courseNames[assignment.courseId] ?: "Course",
                    type = "Assignment"
                )
            }
    val examItems =
        exams
            .filter { exam -> exam.note == null }
            .map { exam ->
                UngradedItem(
                    title = exam.title,
                    courseName = courseNames[exam.courseId] ?: "Course",
                    type = "Exam"
                )
            }

    return assignmentItems + examItems
}
