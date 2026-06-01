package com.done.app.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Assignment
import com.done.app.ui.common.toDisplayDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Composable
fun AssignmentsCard(
    assignment: Assignment,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onAddGradeClick: () -> Unit

) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->

            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
                false
            } else {
                false
            }
        }
    )
    val daysLeft =
        ChronoUnit.DAYS.between(
            LocalDate.now(),
            assignment.date
        )
    val accentColor = when {

        assignment.isDone ->
            Color(0xFF0F766E)

        daysLeft < 0 ->
            Color(0xFFE05A47)

        daysLeft <= 3 ->
            Color(0xFFE05A47)

        else ->
            Color(0xFF7B61FF)
    }
    val statusText = when {
        assignment.isDone -> "Done"
        daysLeft < 0 -> "Overdue"
        daysLeft == 0L -> "Due today"
        daysLeft <= 3 -> "Due soon"
        else -> "Due ${assignment.date.toDisplayDate()}"
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete assignment",
                    tint = Color(0xFFB94040)
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(88.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(width = 5.dp, height = 88.dp)
                        .background(accentColor)
                )

                Checkbox(
                    checked = assignment.isDone,
                    onCheckedChange = onCheckedChange
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = assignment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = accentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit assignment",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = {
                        onAddGradeClick()
                    }
                ) {
                    Text(
                        if (assignment.note == null)
                            "Add Grade"
                        else
                            "Grade: %.2f".format(assignment.note)
                    )
                }
            }
        }
    }
}
