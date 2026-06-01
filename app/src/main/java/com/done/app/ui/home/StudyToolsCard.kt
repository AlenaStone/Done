package com.done.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.done.app.ui.common.toDisplayDate
import kotlinx.coroutines.delay

@Composable
internal fun StudyToolsCard(
    upcomingItems: List<UpcomingItem>,
    ungradedItems: List<UngradedItem>
) {
    var activeTool by remember {
        mutableStateOf<StudyTool?>(null)
    }
    var remainingSeconds by remember {
        mutableStateOf(25 * 60)
    }
    var isFocusRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isFocusRunning, remainingSeconds) {
        if (isFocusRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
        if (remainingSeconds == 0) {
            isFocusRunning = false
        }
    }

    InfoCard(
        title = "Study Tools"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ToolButton(
                title = "Focus block",
                description = "Start a 25 minute focus timer",
                onClick = {
                    activeTool = if (activeTool == StudyTool.Focus) null else StudyTool.Focus
                }
            )
            if (activeTool == StudyTool.Focus) {
                ToolResult {
                    FocusTool(
                        remainingSeconds = remainingSeconds,
                        isRunning = isFocusRunning,
                        onStartPause = {
                            isFocusRunning = !isFocusRunning
                        },
                        onReset = {
                            isFocusRunning = false
                            remainingSeconds = 25 * 60
                        }
                    )
                }
            }

            ToolButton(
                title = "Deadline check",
                description = "Show the closest open deadline",
                onClick = {
                    activeTool = if (activeTool == StudyTool.Deadline) null else StudyTool.Deadline
                }
            )
            if (activeTool == StudyTool.Deadline) {
                ToolResult {
                    DeadlineTool(
                        item = upcomingItems.firstOrNull()
                    )
                }
            }

            ToolButton(
                title = "Grade review",
                description = "Find assignments and exams without grades",
                onClick = {
                    activeTool = if (activeTool == StudyTool.Grades) null else StudyTool.Grades
                }
            )
            if (activeTool == StudyTool.Grades) {
                ToolResult {
                    GradeReviewTool(
                        items = ungradedItems
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolButton(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ToolResult(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        content()
    }
}

@Composable
private fun FocusTool(
    remainingSeconds: Int,
    isRunning: Boolean,
    onStartPause: () -> Unit,
    onReset: () -> Unit
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "%02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onStartPause,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }
            Button(
                onClick = onReset,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
private fun DeadlineTool(
    item: UpcomingItem?
) {
    if (item == null) {
        Text(
            text = "No open deadlines right now.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        ResultList {
            ResultRow(
                title = item.title,
                meta = "${item.courseName} | ${item.type}",
                trailing = item.date.toDisplayDate()
            )
        }
    }
}

@Composable
private fun GradeReviewTool(
    items: List<UngradedItem>
) {
    if (items.isEmpty()) {
        Text(
            text = "All graded assignments and exams are up to date.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        val visibleItems = items.take(4)

        ResultList {
            visibleItems.forEachIndexed { index, item ->
                ResultRow(
                    title = item.title,
                    meta = "${item.courseName} | ${item.type}"
                )
                if (index < visibleItems.lastIndex) {
                    ResultDivider()
                }
            }
        }
    }
}

private enum class StudyTool {
    Focus,
    Deadline,
    Grades
}
