package com.done.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Course

@Composable
fun CourseCard(
    course: Course,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    val status = when {
        course.progress >= 80 -> "Almost Done"
        course.progress >= 50 -> "In Progress"
        else -> "Started"
    }
    val statusColor = when {
        course.progress >= 80 -> Color(0xFF4CAF50)
        course.progress >= 50 -> Color(0xFF2196F3)
        else -> Color(0xFFFFA001)
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete course",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = "$status - ${course.progress}%"
            )

            LinearProgressIndicator(
                progress = {
                    (course.progress / 100f).coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(12.dp),
                color = statusColor,
                trackColor = Color(0xFFE3E5F1),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
        }
    }
}
