package com.done.app.data.model

import java.time.LocalDate

data class Task(

    val id:Int,
    val courseId: Int,
    val name: String,
    val isDone: Boolean = false,
    val deadline: LocalDate
)