package me.ibrahimsn.lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

class TaskProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textBounds = Rect()

    private val sidePadding = 60f

    private val dateRangeSize get() = (width - sidePadding * 2) / 6

    private val tasks = listOf(
        Task("", Calendar.getInstance().time, Calendar.getInstance().apply { this.add(Calendar.DAY_OF_WEEK, 2) }.time, Color.RED)
    )

    private val paintDateText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 40f
    }

    private val paintDateLine = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 2f
    }

    private val paintTask = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        renderDateTexts(canvas)
        renderDateLines(canvas)
        renderTasks(canvas)
    }

    private fun renderDateTexts(canvas: Canvas) {
        for (i in 1..7) {
            canvas.drawTextCentred("$i", dateRangeSize * (i - 1f) + sidePadding, 20f, paintDateText)
        }
    }

    private fun renderDateLines(canvas: Canvas) {
        for (i in 1..7) {
            canvas.drawLine(
                dateRangeSize * (i - 1f) + sidePadding,
                20f + paintDateText.textSize,
                dateRangeSize * (i - 1f) + sidePadding,
                height.toFloat(),
                paintDateLine
            )
        }
    }

    private fun renderTasks(canvas: Canvas) {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)

        val c = Calendar.getInstance()

        for (task in tasks) {
            c.time = task.startDate

            val startDay = c.get(Calendar.DAY_OF_WEEK) % 7
            val startPoint = startDay * dateRangeSize

            c.time = task.endDate

            val endDay = c.get(Calendar.DAY_OF_WEEK) % 7
            val endPoint = endDay * dateRangeSize

            Log.d("###", "start: $startDay end: $endDay")
            Log.d("###", "start: $startPoint end: $endPoint")

            paintTask.color = task.color
            canvas.drawLine(startPoint, 150f, endPoint, 150f, paintTask)
        }
    }

    private fun Canvas.drawTextCentred(text: String, cx: Float, cy: Float, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }
}
