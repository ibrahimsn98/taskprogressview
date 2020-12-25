package me.ibrahimsn.taskprogress

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.ibrahimsn.lib.Task
import me.ibrahimsn.lib.TaskProgressView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = findViewById<TaskProgressView>(R.id.task)

        task.onTaskClickListener = {
            Log.d("###", "Task clicked: $it")
        }

        task.setTasks(
            listOf(
                Task(
                    "",
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 1)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 4)
                    },
                    60,
                    Color.parseColor("#f1c40f")
                ),
                Task(
                    "",
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 2)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 9)
                    },
                    80,
                    Color.parseColor("#5eab3d")
                ),
                Task(
                    "",
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 2)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 5)
                    },
                    40,
                    Color.parseColor("#e74c3c")
                ),
                Task(
                    "",
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 1)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 4)
                    },
                    60,
                    Color.parseColor("#9b59b6")
                ),
                Task(
                    "",
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 1)
                    },
                    Calendar.getInstance().apply {
                        this.add(Calendar.DAY_OF_WEEK, 4)
                    },
                    60,
                    Color.parseColor("#3498db")
                ),
            )
        )

        task.focusRange(Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        })
    }
}
