package me.ibrahimsn.lib

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.abs

class TaskProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textBounds = Rect()

    private val sidePadding = 60f

    private val rangeLength = 7

    private var taskLineWidth = 25f

    private var taskLineSpacing = 80f

    private var head = Calendar.getInstance()

    private val rangeSize get() = (width - sidePadding * 2) / (rangeLength - 1)

    private val taskRect = RectF()

    private val tasks = mutableListOf<Task>()

    private val paintDateText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#5e5e5e")
        textSize = 40f
    }

    private val paintDateLine = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.parseColor("#f1f1f1")
        strokeWidth = 2f
    }

    private val paintTask = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#eaeaea")
        strokeWidth = taskLineWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val paintProgress = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = taskLineWidth
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        renderDateTexts(canvas)
        renderDateLines(canvas)
        renderTasks(canvas)
    }

    private fun renderDateTexts(canvas: Canvas) {
        var start = head.get(Calendar.DAY_OF_MONTH)
        val monthDayCount = head.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..7) {
            if (start > monthDayCount) start = 1
            canvas.drawTextCentred("${start++}", rangeSize * (i - 1f) + sidePadding, 20f, paintDateText)
        }
    }

    private fun renderDateLines(canvas: Canvas) {
        for (i in 1..7) {
            canvas.drawLine(
                rangeSize * (i - 1f) + sidePadding,
                20f + paintDateText.textSize,
                rangeSize * (i - 1f) + sidePadding,
                height.toFloat(),
                paintDateLine
            )
        }
    }

    private fun renderTasks(canvas: Canvas) {
        val viewStartTime = head.timeInMillis
        val viewEndTime = viewStartTime + (rangeLength - 1) * 1000 * 60 * 60 * 24

        for ((i, task) in tasks.withIndex()) {
            val taskStartTime = task.startDate.timeInMillis.toFloat()
            val taskEndTime = task.endDate.timeInMillis.toFloat()

            if (taskEndTime > viewStartTime && taskStartTime < viewEndTime) {
                val taskWidth = ((taskEndTime - taskStartTime) / (1000 * 60 * 60 * 24)) * rangeSize
                val startPoint = sidePadding + (((taskStartTime - head.timeInMillis) / (1000 * 60 * 60 * 24)) * rangeSize)
                val endPoint = startPoint + taskWidth

                paintProgress.color = task.color

                canvas.drawLine(
                    startPoint,
                    paintDateText.textSize + taskLineSpacing * (i + 1),
                    endPoint,
                    paintDateText.textSize + taskLineSpacing * (i + 1),
                    paintTask
                )
                canvas.drawLine(
                    startPoint,
                    paintDateText.textSize + taskLineSpacing * (i + 1),
                    startPoint + (endPoint - startPoint) / 100 * task.progress,
                    paintDateText.textSize + taskLineSpacing * (i + 1),
                    paintProgress
                )
            }
        }
    }

    private fun getTaskRect(task: Task): RectF {
        val taskStartTime = task.startDate.timeInMillis.toFloat()
        val taskEndTime = task.endDate.timeInMillis.toFloat()

        val taskWidth = ((taskEndTime - taskStartTime) / (1000 * 60 * 60 * 24)) * rangeSize
        val startPoint = sidePadding + (((taskStartTime - head.timeInMillis) / (1000 * 60 * 60 * 24)) * rangeSize)
        val endPoint = startPoint + taskWidth

        val taskIndex = tasks.indexOf(task)

        taskRect.set(
            startPoint,
            paintDateText.textSize + taskLineSpacing * (taskIndex + 1) - taskLineWidth,
            endPoint,
            paintDateText.textSize + taskLineSpacing * (taskIndex + 1) + taskLineWidth
        )
        return taskRect
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        invalidate()
    }

    fun focusRange(head: Calendar) {
        post {
            val animator = ValueAnimator.ofFloat(this.head.timeInMillis.toFloat(), head.timeInMillis.toFloat())

            animator.addUpdateListener {
                this.head.timeInMillis = (it.animatedValue as Float).toLong()
                invalidate()
            }

            animator.duration = 500
            animator.start()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < 500) {
            for (task in tasks) {
                if (getTaskRect(task).contains(event.x, event.y)) {
                    focusRange(task.startDate)
                }
            }
        }

        return true
    }

    private fun Canvas.drawTextCentred(text: String, cx: Float, cy: Float, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }
}
