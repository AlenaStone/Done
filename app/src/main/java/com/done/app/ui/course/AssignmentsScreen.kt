package com.done.app.ui.course

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.done.app.data.model.Assignment
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.AssignmentViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    viewModel: AssignmentViewModel
) {
    var newAssignmentName by remember {
        mutableStateOf("")
    }

    var showAddAssignDialog by remember {
        mutableStateOf(false)
    }
    var newDeadline by remember {
        mutableStateOf("")
    }
    val newAssignmentNameTrimmed = newAssignmentName.trim()

    val deadlineDate = try {
        LocalDate.parse(newDeadline)
    } catch (e: Exception) {
        null
    }

    val assignments by viewModel.assignments.collectAsState()

    var showGradeDialog by remember {
        mutableStateOf(false)
    }

    var selectedAssignment by remember {
        mutableStateOf<Assignment?>(null)
    }

    var gradeText by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text("Assignments")
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
                    showAddAssignDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add assignment"
                )
            }
        }
    ) { paddingValues ->
        if (showAddAssignDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddAssignDialog = false
                },
                title = {
                    Text("Add Assignment")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newAssignmentName,
                            onValueChange = {
                                newAssignmentName = it
                            },
                            label = {
                                Text("Assignment Name")
                            }
                        )

                        OutlinedTextField(
                            value = newDeadline,
                            onValueChange = {
                                newDeadline = it
                            },
                            label = {
                                Text("Date (YYYY-MM-DD)")
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        enabled = newAssignmentNameTrimmed.isNotEmpty() && deadlineDate != null,
                        onClick = {
                            if (newAssignmentNameTrimmed.isNotEmpty()
                                && deadlineDate != null
                            ) {
                                viewModel.addAssignment(
                                    assignmentName = newAssignmentNameTrimmed,
                                    date = deadlineDate
                                )

                                newAssignmentName = ""
                                newDeadline = ""
                                showAddAssignDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddAssignDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (showGradeDialog) {

            AlertDialog(
                onDismissRequest = {
                    showGradeDialog = false
                },

                title = {
                    Text("Add Grade")
                },

                text = {

                    OutlinedTextField(
                        value = gradeText,
                        onValueChange = {
                            gradeText = it
                        },
                        label = {
                            Text("Grade")
                        }
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            val grade =
                                gradeText.toDoubleOrNull()

                            if (
                                grade != null &&
                                selectedAssignment != null
                            ) {

                                selectedAssignment?.let { assignment ->
                                    viewModel.updateAssignment(
                                        assignment.copy(
                                            note = grade
                                        )
                                    )
                                }

                                showGradeDialog = false
                                gradeText = ""
                            }
                        }
                    ) {
                        Text("Save")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            showGradeDialog = false
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
            if (assignments.isEmpty()) {
                EmptyState(
                    text = "No assignments yet. Tap + to add one."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(assignments) { assignment ->
                        AssignmentsCard(
                            assignment = assignment,
                            onAddGradeClick = {
                                selectedAssignment = assignment
                                gradeText = assignment.note?.toString() ?: ""
                                showGradeDialog = true
                            },
                            onDeleteClick = {
                                viewModel.deleteAssignment(assignment)
                            },
                            onCheckedChange =  { isChecked ->
                                viewModel.updateAssignment(
                                    assignment.copy(
                                        isDone = isChecked
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
