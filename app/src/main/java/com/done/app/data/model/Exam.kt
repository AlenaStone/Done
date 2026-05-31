package com.done.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Exam(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val courseId: Int,
    val title: String,
    val date: LocalDate,
    val isDone: Boolean = false,
    val note: Double? = null
)
