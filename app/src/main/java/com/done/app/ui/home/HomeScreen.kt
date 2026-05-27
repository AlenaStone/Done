package com.done.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.done.app.data.model.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var newCourseName by remember {
        mutableStateOf("")
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    var nextId by remember {
        mutableIntStateOf(4)
    }
    var showWindowDialog by remember {
        mutableStateOf(false)
    }

    val courses = remember {
        mutableStateListOf(
            Course(
                id = 1,
                name = "Mobile Development",
                progress = 80
            ),
            Course(
                id = 2,
                name = "Cloud Computing",
                progress = 20
            ),
            Course(
                id = 3,
                name = "Web Engineering",
                progress = 50
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {
        Column(
            modifier = Modifier.background(
                Color(0xFFEDEAFB)
            )
        ) {

            TopAppBar(
                title = {
                    Text("Done")
                },
                actions = {

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {  Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open navigation menu"
                )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Add Course")
                        },
                        onClick = {

                            expanded = false
                            showWindowDialog = true

                        }
                    )
                }
            }

        )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD5D0F0)
            )
        }

        if (showWindowDialog) {

            AlertDialog(
                onDismissRequest = {
                    showWindowDialog = false
                },

                title = {
                    Text("Add Course")
                },

                text = {
                    OutlinedTextField(
                        value = newCourseName,
                        onValueChange = {
                            newCourseName = it
                        },
                        label = {
                            Text("Course Name")
                        }
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {
                            if (newCourseName.isNotBlank()) {

                                courses.add(
                                    Course(
                                        id = nextId,
                                        name = newCourseName,
                                        progress = 0
                                    )
                                )

                                nextId++

                                newCourseName = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            showWindowDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {

            items(courses) { course ->

                CourseCard(
                    course = course,
                    onDeleteClick = {
                        courses.remove(course)
                    }
                )

            }
        }
        }
    }

