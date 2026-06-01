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
import com.done.app.data.model.Exam
import com.done.app.ui.common.AddDatedItemDialog
import com.done.app.ui.common.AddGradeDialog
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.ExamViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    navController: NavController,
    viewModel: ExamViewModel
) {
    var newExamName by remember {
        mutableStateOf("")
    }
    var showAddExamDialog by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    var editingExam by remember {
        mutableStateOf<Exam?>(null)
    }
    var editExamName by remember {
        mutableStateOf("")
    }
    var editExamDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    val newExamNameTrimmed = newExamName.trim()
    val editExamNameTrimmed = editExamName.trim()

    var showGradeDialog by remember {
        mutableStateOf(false)
    }

    var selectedExam by remember {
        mutableStateOf<Exam?>(null)
    }

    var gradeText by remember {
        mutableStateOf("")
    }
    val grade = gradeText.toDoubleOrNull()
    val isGradeValid = grade != null

    val exams by viewModel.exams.collectAsState()

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
                        Text("Exams")
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
                    showAddExamDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add exam"
                )
            }
        }
    ) { paddingValues ->
        if (showAddExamDialog) {
            AddDatedItemDialog(
                title = "Add Exam",
                name = newExamName,
                nameLabel = "Exam name",
                selectedDate = selectedDate,
                datePlaceholder = "Select exam date",
                canSave = newExamNameTrimmed.isNotEmpty() && selectedDate != null,
                onNameChange = {
                    newExamName = it
                },
                onDateSelected = {
                    selectedDate = it
                },
                onConfirm = {
                    selectedDate?.let { date ->
                        viewModel.addExam(
                            examName = newExamNameTrimmed,
                            date = date
                        )

                        newExamName = ""
                        selectedDate = null
                        showAddExamDialog = false
                    }
                },
                onDismiss = {
                    showAddExamDialog = false
                }
            )
        }
        editingExam?.let { exam ->
            AddDatedItemDialog(
                title = "Edit Exam",
                name = editExamName,
                nameLabel = "Exam name",
                selectedDate = editExamDate,
                datePlaceholder = "Select exam date",
                canSave = editExamNameTrimmed.isNotEmpty() && editExamDate != null,
                confirmText = "Save",
                onNameChange = {
                    editExamName = it
                },
                onDateSelected = {
                    editExamDate = it
                },
                onConfirm = {
                    editExamDate?.let { date ->
                        viewModel.updateExam(
                            exam.copy(
                                title = editExamNameTrimmed,
                                date = date
                            )
                        )

                        editingExam = null
                        editExamName = ""
                        editExamDate = null
                    }
                },
                onDismiss = {
                    editingExam = null
                    editExamName = ""
                    editExamDate = null
                }
            )
        }
        if (showGradeDialog) {
            AddGradeDialog(
                gradeText = gradeText,
                isGradeValid = selectedExam != null && isGradeValid,
                onGradeChange = {
                    gradeText = it
                },
                onConfirm = {
                    if (
                        grade != null &&
                        selectedExam != null
                    ) {
                        selectedExam?.let { exam ->
                            viewModel.updateExam(
                                exam.copy(
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
            if (exams.isEmpty()) {
                EmptyState(
                    text = "No exams yet. Tap + to add one."
                )
            } else {
                val sortedExams =
                    exams.sortedWith(
                        compareBy<Exam> { it.isDone }
                            .thenBy { it.date }
                    )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = sortedExams,
                        key = { exam -> exam.id }
                    ) { exam ->
                        ExamsCard(
                            exam = exam,
                            onAddGradeClick = {
                                selectedExam = exam
                                gradeText = exam.note?.toString() ?: ""
                                showGradeDialog = true
                            },
                            onDeleteClick = {
                                viewModel.deleteExam(exam)
                            },
                            onEditClick = {
                                editingExam = exam
                                editExamName = exam.title
                                editExamDate = exam.date
                            },
                            onCheckedChange =  { isChecked ->
                                viewModel.updateExam(
                                    exam.copy(
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
