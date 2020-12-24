package me.ibrahimsn.lib

import androidx.annotation.ColorInt
import java.util.*

data class Task(
    val id: String,
    val startDate: Date,
    val endDate: Date,
    @ColorInt val color: Int
)
