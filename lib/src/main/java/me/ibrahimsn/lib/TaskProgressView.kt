package me.ibrahimsn.lib

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import java.util.*
import kotlin.math.abs

class TaskProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Attribute Defaults
    private var _sidePadding = 60f

    private var _rangeLength = 7

    private var _taskDateMargin = 20f

    private var _taskDateTextSize = 40f

    private var _taskLineSpacing = 80f

    // Constant Objects
    private val textBounds = Rect()

    private val taskRect = RectF()

    private val tasks = mutableListOf<Task>()

    private var head = Calendar.getInstance()

    // Global Getters
    private val rangeSize get() = (width - sidePadding * 2) / (rangeLength - 1)

    private val viewStartTime get() = head.timeInMillis

    private val viewEndTime get() = viewStartTime + (rangeLength - 1) * DAY_MULTIPLIER

    // Animators
    private val animator = ValueAnimator.ofFloat().apply {
        duration = ANIMATION_DURATION
        interpolator = OvershootInterpolator()
    }

    // Core Attributes
    var sidePadding: Float
        get() = _sidePadding
        set(value) {
            _sidePadding = value
            invalidate()
        }

    var rangeLength: Int
        get() = _rangeLength
        set(value) {
            _rangeLength = value
            invalidate()
        }

    var taskDateMargin: Float
        get() = _taskDateMargin
        set(value) {
            _taskDateMargin = value
            invalidate()
        }

    var taskDateTextSize: Float
        get() = _taskDateTextSize
        set(value) {
            _taskDateTextSize = value
            invalidate()
        }

    var taskLineWidth: Float
        get() = paintTask.strokeWidth
        set(value) {
            paintTask.strokeWidth = value
            paintProgress.strokeWidth = value
            invalidate()
        }

    var taskLineSpacing: Float
        get() = _taskLineSpacing
        set(value) {
            _taskLineSpacing = value
            invalidate()
        }

    var taskDateTextColor: Int
        get() = paintDateText.color
        set(value) {
            paintDateText.color = value
            invalidate()
        }

    // Listeners
    var onTaskClickListener: ((Task) -> Unit)? = null

    // Paints
    private val paintDateText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_DATE_TEXT_COLOR
        textSize = taskDateTextSize
    }

    private val paintDateLine = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = DEFAULT_DATE_LINE_COLOR
        strokeWidth = TASK_STROKE_WIDTH
    }

    private val paintTask = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = DEFAULT_TASK_BACKGROUND_COLOR
        strokeWidth = TASK_LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private val paintProgress = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = TASK_LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TaskProgressView,
            defStyleAttr,
            0
        )

        try {
            with(typedArray) {
                sidePadding = getDimension(
                    R.styleable.TaskProgressView_sidePadding,
                    sidePadding
                )

                rangeLength = getInt(
                    R.styleable.TaskProgressView_rangeLength,
                    rangeLength
                )

                taskDateMargin = getDimension(
                    R.styleable.TaskProgressView_taskDateMargin,
                    taskDateMargin
                )

                taskDateTextSize = getDimension(
                    R.styleable.TaskProgressView_taskDateTextSize,
                    taskDateTextSize
                )

                taskLineWidth = getDimension(
                    R.styleable.TaskProgressView_taskLineWidth,
                    taskLineWidth
                )

                taskLineSpacing = getDimension(
                    R.styleable.TaskProgressView_taskLineSpacing,
                    taskLineSpacing
                )

                taskDateTextColor = getColor(
                    R.styleable.TaskProgressView_taskDateTextColor,
                    taskDateTextColor
                )
            }
        } catch (e: Exception) {
            // ignored
        } finally {
            typedArray.recycle()
        }
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

        for (i in 1..rangeLength) {
            if (start > monthDayCount) start = 1
            canvas.drawTextCentred("${start++}", rangeSize * (i - 1f) + sidePadding, 20f, paintDateText)
        }
    }

    private fun renderDateLines(canvas: Canvas) {
        for (i in 1..rangeLength) {
            canvas.drawLine(
                rangeSize * (i - 1) + sidePadding,
                taskDateMargin + paintDateText.textSize,
                rangeSize * (i - 1) + sidePadding,
                height.toFloat(),
                paintDateLine
            )
        }
    }

    private fun renderTasks(canvas: Canvas) {
        for (task in tasks) {
            if (task.endTime > viewStartTime - DAY_MULTIPLIER && task.startTime < viewEndTime + DAY_MULTIPLIER) {
                val taskWidth = ((task.endTime - task.startTime) / DAY_MULTIPLIER) * rangeSize
                val startPoint = sidePadding + (((task.startTime - head.timeInMillis) / DAY_MULTIPLIER) * rangeSize)
                val endPoint = startPoint + taskWidth

                paintProgress.color = task.color

                canvas.drawLine(
                    startPoint,
                    taskDateTextSize + taskLineSpacing * task.category,
                    endPoint,
                    taskDateTextSize + taskLineSpacing * task.category,
                    paintTask
                )
                canvas.drawLine(
                    startPoint,
                    taskDateTextSize + taskLineSpacing * task.category,
                    startPoint + (endPoint - startPoint) / 100 * task.progress,
                    taskDateTextSize + taskLineSpacing * task.category,
                    paintProgress
                )
            }
        }
    }

    private fun getTaskRect(task: Task): RectF {
        val taskWidth = ((task.endTime - task.startTime) / DAY_MULTIPLIER) * rangeSize
        val startPoint = sidePadding + (((task.startTime - head.timeInMillis) / DAY_MULTIPLIER) * rangeSize)
        val endPoint = startPoint + taskWidth

        taskRect.set(
            startPoint,
            taskDateTextSize + taskLineSpacing * task.category - taskLineWidth,
            endPoint,
            taskDateTextSize + taskLineSpacing * task.category + taskLineWidth
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
            animator.setFloatValues(
                this.head.timeInMillis.toFloat(),
                head.timeInMillis.toFloat()
            )
            animator.start()
        }
    }

    private val animatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        head.timeInMillis = (it.animatedValue as Float).toLong()
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < TOUCH_EVENT_DURATION) {
            for (task in tasks) {
                if (getTaskRect(task).contains(event.x, event.y)) {
                    onTaskClickListener?.invoke(task)
                    focusRange(task.startDate)
                    return true
                }
            }
        }
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.addUpdateListener(animatorUpdateListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.removeUpdateListener(animatorUpdateListener)
        animator.cancel()
    }

    private fun Canvas.drawTextCentred(text: String, cx: Float, cy: Float, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }

    companion object {
        private const val DAY_MULTIPLIER = 86400000 // 1000 * 60 * 60 * 24
        private const val TOUCH_EVENT_DURATION = 500
        private const val TASK_STROKE_WIDTH = 2f
        private const val ANIMATION_DURATION = 500L
        private const val TASK_LINE_WIDTH = 25f

        private val DEFAULT_DATE_TEXT_COLOR = Color.parseColor("#5e5e5e")
        private val DEFAULT_DATE_LINE_COLOR = Color.parseColor("#f1f1f1")
        private val DEFAULT_TASK_BACKGROUND_COLOR = Color.parseColor("#eaeaea")
    }
}
