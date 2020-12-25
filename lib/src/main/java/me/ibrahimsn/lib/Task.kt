package me.ibrahimsn.lib

import androidx.annotation.ColorInt
import java.util.*

data class Task(
    val id: String,
    val startDate: Calendar,
    val endDate: Calendar,
    val progress: Int,
    @ColorInt val color: Int,
)

val Task.startTime get() = startDate.timeInMillis.toFloat()

val Task.endTime get() = endDate.timeInMillis.toFloat()