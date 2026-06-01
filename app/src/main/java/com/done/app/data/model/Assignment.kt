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
data class Assignment(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val courseId: Int,
        val title: String,
        val date: LocalDate,
        val isDone: Boolean = false,
        val note: Double? = null
)
