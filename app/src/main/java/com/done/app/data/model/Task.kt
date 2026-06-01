package com.done.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("courseId")
    ]
)
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val courseId: Int,

    val name: String,

    val isDone: Boolean = false,

    val deadline: LocalDate
)
