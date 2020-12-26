package me.ibrahimsn.lib

import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import java.util.*

data class Task(
    val id: Int,
    val startDate: Calendar,
    val endDate: Calendar,
    @IntRange(from = 0, to = 100) val progress: Int,
    val category: Int,
    @ColorInt val color: Int,
)

val Task.startTime get() = startDate.timeInMillis.toFloat()

val Task.endTime get() = endDate.timeInMillis.toFloat()

val Task.duration get() = (endTime - startTime) / 86400000