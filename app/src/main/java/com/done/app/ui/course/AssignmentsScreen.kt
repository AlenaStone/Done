package com.done.app.ui.course

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.done.app.ui.common.AddDatedItemDialog
import com.done.app.ui.common.AddGradeDialog
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.AssignmentViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    navController: NavController,
    viewModel: AssignmentViewModel
) {
    var newAssignmentName by remember {
        mutableStateOf("")
    }

    var showAddAssignDialog by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    var editingAssignment by remember {
        mutableStateOf<Assignment?>(null)
    }
    var editAssignmentName by remember {
        mutableStateOf("")
    }
    var editAssignmentDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    val newAssignmentNameTrimmed = newAssignmentName.trim()
    val editAssignmentNameTrimmed = editAssignmentName.trim()

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
    val grade = gradeText.toDoubleOrNull()
    val isGradeValid = grade != null

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
                        Text("Assignments")
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
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
            AddDatedItemDialog(
                title = "Add Assignment",
                name = newAssignmentName,
                nameLabel = "Assignment name",
                selectedDate = selectedDate,
                datePlaceholder = "Select due date",
                canSave = newAssignmentNameTrimmed.isNotEmpty() && selectedDate != null,
                onNameChange = {
                    newAssignmentName = it
                },
                onDateSelected = {
                    selectedDate = it
                },
                onConfirm = {
                    selectedDate?.let { date ->
                        viewModel.addAssignment(
                            assignmentName = newAssignmentNameTrimmed,
                            date = date
                        )

                        newAssignmentName = ""
                        selectedDate = null
                        showAddAssignDialog = false
                    }
                },
                onDismiss = {
                    showAddAssignDialog = false
                }
            )
        }
        editingAssignment?.let { assignment ->
            AddDatedItemDialog(
                title = "Edit Assignment",
                name = editAssignmentName,
                nameLabel = "Assignment name",
                selectedDate = editAssignmentDate,
                datePlaceholder = "Select due date",
                canSave = editAssignmentNameTrimmed.isNotEmpty() && editAssignmentDate != null,
                confirmText = "Save",
                onNameChange = {
                    editAssignmentName = it
                },
                onDateSelected = {
                    editAssignmentDate = it
                },
                onConfirm = {
                    editAssignmentDate?.let { date ->
                        viewModel.updateAssignment(
                            assignment.copy(
                                title = editAssignmentNameTrimmed,
                                date = date
                            )
                        )

                        editingAssignment = null
                        editAssignmentName = ""
                        editAssignmentDate = null
                    }
                },
                onDismiss = {
                    editingAssignment = null
                    editAssignmentName = ""
                    editAssignmentDate = null
                }
            )
        }
        if (showGradeDialog) {
            AddGradeDialog(
                gradeText = gradeText,
                isGradeValid = selectedAssignment != null && isGradeValid,
                onGradeChange = {
                    gradeText = it
                },
                onConfirm = {
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
                },
                onDismiss = {
                    showGradeDialog = false
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (assignments.isEmpty()) {
                EmptyState(
                    text = "No assignments yet. Tap + to add one."
                )
            } else {
                val sortedAssignments =
                    assignments.sortedWith(
                        compareBy<Assignment> { it.isDone }
                            .thenBy { it.date }
                    )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = sortedAssignments,
                        key = { assignment -> assignment.id }
                    ) { assignment ->
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
                            onEditClick = {
                                editingAssignment = assignment
                                editAssignmentName = assignment.title
                                editAssignmentDate = assignment.date
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
