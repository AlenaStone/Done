package com.done.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val courseId: Int,

    val name: String,

    val isDone: Boolean = false,

    val deadline: LocalDate
)