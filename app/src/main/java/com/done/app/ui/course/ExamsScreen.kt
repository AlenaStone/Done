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
import com.done.app.data.model.Exam
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.ExamViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    viewModel: ExamViewModel
) {
    var newExamName by remember {
        mutableStateOf("")
    }
    var showAddExamDialog by remember {
        mutableStateOf(false)
    }
    var newDeadline by remember {
        mutableStateOf("")
    }
    val newExamNameTrimmed = newExamName.trim()

    val deadlineDate = try {
        LocalDate.parse(newDeadline)
    } catch (e: Exception) {
        null
    }

    var showGradeDialog by remember {
        mutableStateOf(false)
    }

    var selectedExam by remember {
        mutableStateOf<Exam?>(null)
    }

    var gradeText by remember {
        mutableStateOf("")
    }

    val exams by viewModel.exams.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text("Exams")
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
            AlertDialog(
                onDismissRequest = {
                    showAddExamDialog = false
                },
                title = {
                    Text("Add Exam")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newExamName,
                            onValueChange = {
                                newExamName = it
                            },
                            label = {
                                Text("Exam Name")
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
                        enabled = newExamNameTrimmed.isNotEmpty() && deadlineDate != null,
                        onClick = {
                            if (newExamNameTrimmed.isNotEmpty()
                                && deadlineDate != null
                            ) {
                                viewModel.addExam(
                                    examName = newExamNameTrimmed,
                                    date = deadlineDate
                                )

                                newExamName = ""
                                newDeadline = ""
                                showAddExamDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddExamDialog = false
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
            if (exams.isEmpty()) {
                EmptyState(
                    text = "No exams yet. Tap + to add one."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(exams) { exam ->
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
