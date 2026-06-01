package com.done.app.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun AddDatedItemDialog(
    title: String,
    name: String,
    nameLabel: String,
    selectedDate: LocalDate?,
    datePlaceholder: String,
    canSave: Boolean,
    confirmText: String = "Add",
    onNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(8.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = {
                        Text(nameLabel)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerField(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected,
                    placeholder = datePlaceholder
                )
            }
        },
        confirmButton = {
            Button(
                enabled = canSave,
                onClick = onConfirm,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
