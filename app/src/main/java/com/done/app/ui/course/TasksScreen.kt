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
import com.done.app.data.model.Task
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    courseName: String
) {
    var newTaskName by remember {
        mutableStateOf("")
    }
    var nextId by remember {
        mutableIntStateOf(4)
    }
    var showAddTaskDialog by remember {
        mutableStateOf(false)
    }
    var newDeadline by remember {
        mutableStateOf("")
    }
    val newTaskNameTrimmed = newTaskName.trim()

    val deadlineDate = try {
        LocalDate.parse(newDeadline)
    } catch (e: Exception) {
        null
    }

    val tasks = remember {
        mutableStateListOf(
            Task(
                id = 1,
                courseId = 1,
                name = "Java Basics",
                deadline = LocalDate.now()
            ),
            Task(
                id = 2,
                courseId = 1,
                name = "Compose Layouts",
                deadline = LocalDate.now()
            ),
            Task(
                id = 3,
                courseId = 1,
                name = "Navigation",
                deadline = LocalDate.now()
            )
        )
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
                    showAddTaskDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task"
                )
            }
        }
    ) { paddingValues ->
        if (showAddTaskDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddTaskDialog = false
                },
                title = {
                    Text("Add Task")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTaskName,
                            onValueChange = {
                                newTaskName = it
                            },
                            label = {
                                Text("Task Name")
                            }
                        )

                        OutlinedTextField(
                            value = newDeadline,
                            onValueChange = {
                                newDeadline = it
                            },
                            label = {
                                Text("Deadline (YYYY-MM-DD)")
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        enabled = newTaskNameTrimmed.isNotEmpty(),
                        onClick = {
                            if (newTaskNameTrimmed.isNotEmpty()
                                && deadlineDate != null) {
                                tasks.add(
                                    Task(
                                        id = nextId,
                                        courseId = 1,
                                        name = newTaskNameTrimmed,
                                        deadline = deadlineDate
                                    )
                                )

                                nextId++
                                newTaskName = ""
                                showAddTaskDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddTaskDialog = false
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
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks yet",
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
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            onDeleteClick = {
                                tasks.remove(task)
                            },
                            onCheckedChange = { isChecked ->
                                val index = tasks.indexOf(task)
                                tasks[index] = task.copy(
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
