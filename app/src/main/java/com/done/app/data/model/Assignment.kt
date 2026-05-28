package com.done.app.data.model

import java.time.LocalDate

data  class Assignment (
        val id: Int,
        val courseId: Int,
        val title: String,
        val date: LocalDate,
        val isDone: Boolean = false,
        val note: Double? = null
)