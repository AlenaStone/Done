package com.done.app.ui.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val displayDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd MMM yyyy")

fun LocalDate.toDisplayDate(): String {
    return format(displayDateFormatter)
}
