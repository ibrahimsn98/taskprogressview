package me.ibrahimsn.taskprogress

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import me.ibrahimsn.lib.Task
import me.ibrahimsn.lib.TaskProgressView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskProgressView = findViewById<TaskProgressView>(R.id.taskProgressView)

        val buttonToday = findViewById<Button>(R.id.buttonToday)
        val buttonNextWeek = findViewById<Button>(R.id.buttonNextWeek)

        taskProgressView.onTaskClickListener = {
            Log.d("###", "On task click: $it")
        }

        taskProgressView.setTasks(
            listOf(
                Task(
                    0,
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 1)
                    },
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 4)
                    },
                    60,
                    1,
                    Color.parseColor("#f1c40f")
                ),
                Task(
                    1,
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 2)
                    },
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 9)
                    },
                    80,
                    2,
                    Color.parseColor("#5eab3d")
                ),
                Task(
                    2,
                    Calendar.getInstance(),
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 5)
                    },
                    40,
                    3,
                    Color.parseColor("#e74c3c")
                ),
                Task(
                    3,
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 8)
                    },
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 11)
                    },
                    40,
                    3,
                    Color.parseColor("#e74c3c")
                ),
                Task(
                    4,
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 5)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 12)
                    },
                    60,
                    4,
                    Color.parseColor("#9b59b6")
                ),
                Task(
                    5,
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 1)
                    },
                    Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK, 4)
                    },
                    60,
                    5,
                    Color.parseColor("#3498db")
                ),
            )
        )

        taskProgressView.focusRange(Calendar.getInstance())

        buttonToday.setOnClickListener {
            taskProgressView.focusRange(Calendar.getInstance())
        }

        buttonNextWeek.setOnClickListener {
            taskProgressView.focusRange(Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_MONTH, 1)
            })
        }
    }
}
