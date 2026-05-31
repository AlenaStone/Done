package com.done.app.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(
        date: LocalDate?
    ): String? {
        return date?.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(
        value: String?
    ): LocalDate? {
        return value?.let {
            LocalDate.parse(it)
        }
    }
}