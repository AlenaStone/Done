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
import com.done.app.data.model.Task
import com.done.app.ui.common.AddDatedItemDialog
import com.done.app.ui.common.EmptyState
import com.done.app.viewmodel.TaskViewModel
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    var newTaskName by remember {
        mutableStateOf("")
    }
    var showAddTaskDialog by remember {
        mutableStateOf(false)
    }
    var selectedDeadline by remember {
        mutableStateOf<LocalDate?>(null)
    }
    var editingTask by remember {
        mutableStateOf<Task?>(null)
    }
    var editTaskName by remember {
        mutableStateOf("")
    }
    var editTaskDeadline by remember {
        mutableStateOf<LocalDate?>(null)
    }
    val newTaskNameTrimmed = newTaskName.trim()
    val editTaskNameTrimmed = editTaskName.trim()

    val tasks by viewModel.tasks.collectAsState()

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
                        Text("Tasks")
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
            AddDatedItemDialog(
                title = "Add Task",
                name = newTaskName,
                nameLabel = "Task name",
                selectedDate = selectedDeadline,
                datePlaceholder = "Select deadline",
                canSave = newTaskNameTrimmed.isNotEmpty() && selectedDeadline != null,
                onNameChange = {
                    newTaskName = it
                },
                onDateSelected = {
                    selectedDeadline = it
                },
                onConfirm = {
                    selectedDeadline?.let { deadline ->
                        viewModel.addTask(
                            taskName = newTaskNameTrimmed,
                            deadline = deadline
                        )

                        newTaskName = ""
                        selectedDeadline = null
                        showAddTaskDialog = false
                    }
                },
                onDismiss = {
                    showAddTaskDialog = false
                }
            )
        }
        editingTask?.let { task ->
            AddDatedItemDialog(
                title = "Edit Task",
                name = editTaskName,
                nameLabel = "Task name",
                selectedDate = editTaskDeadline,
                datePlaceholder = "Select deadline",
                canSave = editTaskNameTrimmed.isNotEmpty() && editTaskDeadline != null,
                confirmText = "Save",
                onNameChange = {
                    editTaskName = it
                },
                onDateSelected = {
                    editTaskDeadline = it
                },
                onConfirm = {
                    editTaskDeadline?.let { deadline ->
                        viewModel.updateTask(
                            task.copy(
                                name = editTaskNameTrimmed,
                                deadline = deadline
                            )
                        )

                        editingTask = null
                        editTaskName = ""
                        editTaskDeadline = null
                    }
                },
                onDismiss = {
                    editingTask = null
                    editTaskName = ""
                    editTaskDeadline = null
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (tasks.isEmpty()) {
                EmptyState(
                    text = "No tasks yet. Tap + to add one."
                )
            } else {
                val sortedTasks =
                    tasks.sortedWith(
                        compareBy<Task> { it.isDone }
                            .thenBy { it.deadline }
                    )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = sortedTasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onDeleteClick = {
                                viewModel.deleteTask(task)
                            },
                            onEditClick = {
                                editingTask = task
                                editTaskName = task.name
                                editTaskDeadline = task.deadline
                            },
                            onCheckedChange = { isChecked ->

                                viewModel.updateTask(
                                    task.copy(
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
