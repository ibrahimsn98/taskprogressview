package me.ibrahimsn.lib

import androidx.annotation.ColorInt
import java.util.*

data class Task(
    val id: Int,
    val startDate: Calendar,
    val endDate: Calendar,
    val progress: Int,
    val category: Int,
    @ColorInt val color: Int,
)

val Task.startTime get() = startDate.timeInMillis.toFloat()

val Task.endTime get() = endDate.timeInMillis.toFloat()

val Task.duration get() = (endTime - startTime) / 86400000