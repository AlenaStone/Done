package com.done.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val totalClasses: Int = 0,

    val missedClasses: Int = 0
)
