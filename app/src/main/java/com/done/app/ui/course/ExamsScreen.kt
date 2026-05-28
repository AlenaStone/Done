package com.done.app.ui.course

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Exam
import com.done.app.data.repository.AppRepository
import com.done.app.data.repository.AppRepository.courses
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun ExamScreen(
        courseName: String
    ) {
    var newExamName by remember {
        mutableStateOf("")
    }
    var nextId by remember {
        mutableIntStateOf(0)
    }
    var showAddExamDialog by remember {
        mutableStateOf(false)
    }
    var newDeadline by remember {
        mutableStateOf("")
    }
    val exams = AppRepository.exams
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

    val course =  courses.find {
            it.name == courseName
        }  ?: return
    val courseExams =
        exams.filter {
            it.courseId == course.id
        }
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color(0xFFEDEAFB))
            ) {
                TopAppBar(
                    title = {
                        Text(courseName)
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
                        enabled = newExamNameTrimmed.isNotEmpty(),
                        onClick = {
                            if (newExamNameTrimmed.isNotEmpty()
                                && deadlineDate != null
                            ) {
                                exams.add(
                                    Exam(
                                        id = nextId,
                                        courseId = course?.id ?: 0,
                                        title = newExamNameTrimmed,
                                        date = deadlineDate,
                                        isDone = false
                                    )
                                )

                                nextId++
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

                                val index =
                                    exams.indexOf(selectedExam)

                                exams[index] =
                                    selectedExam!!.copy(
                                        note = grade
                                    )

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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No exams yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6D6A7A)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(courseExams) { exam ->
                        ExamsCard(
                            exam = exam,
                            onAddGradeClick = {
                                selectedExam = exam
                                gradeText = exam.note?.toString() ?: ""
                                showGradeDialog = true
                            },
                            onDeleteClick = {
                                exams.remove(exam)
                            },
                            onCheckedChange =  { isChecked ->
                            val index = exams.indexOf(exam)
                            exams[index] = exam.copy(
                                isDone = isChecked
                            )
                        }
                        )
                    }
                }
            }
        }
    }
}
