package com.done.app.ui.course

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Exam
import com.done.app.data.model.Task
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamsCard(
    exam: Exam,
    onDeleteClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->

            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
                true
            } else {
                false
            }
        }
    )
    val daysLeft =
        ChronoUnit.DAYS.between(
            LocalDate.now(),
            exam.date
        )
    val cardColor = when {

        exam.isDone ->
            Color(0xFF6BA473)

        daysLeft < 0 ->
            Color(0xDFB94040)

        daysLeft <= 3 ->
            Color(0xFFEEC4C9)

        else ->
            Color(0xFFC4C4D2)
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
                    contentDescription = null
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp),

            colors = CardDefaults.cardColors(
                containerColor = cardColor
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = exam.isDone,
                    onCheckedChange = onCheckedChange
                )
                Text(
                    text = exam.title,
                    modifier = Modifier.weight(1f),

                    )

                Text(
                    text = exam.date.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )


            }
        }
    }
}

