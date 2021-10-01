package com.boltic28.coroutinescompose.elements.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boltic28.coroutinescompose.TaskManager
import com.boltic28.coroutinescompose.elements.buttons.AppTextButton
import com.boltic28.coroutinescompose.elements.buttons.ButtonStyle
import com.boltic28.coroutinescompose.workers.AsyncTask
import com.boltic28.coroutinescompose.workers.Task

@Composable
fun CoroutineItemAsync(task: AsyncTask, taskWorker: TaskManager) {
    val work1status = remember { task.observeProgress1() }.collectAsState("")
    val work2status = remember { task.observeProgress2() }.collectAsState("")
    val work3status = remember { task.observeProgress3() }.collectAsState("")
    val log = remember { task.observeLog() }.collectAsState("")
    val result = remember { task.observeResult() }.collectAsState("")
    val statusState = remember { task.observeStatus() }
        .collectAsState(initial = Task.Status.PENDING)

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Sync/Async test #${task.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(text = "work 1 status: ${work1status.value}")
        Text(text = "work 2 status: ${work2status.value}")
        Text(text = "work 3 status: ${work3status.value}")
        Text(
            text = "Coroutine status: ${statusState.value}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Text(text = "Log: ${log.value}")
        Text(
            text = result.value,
            modifier = Modifier.align(alignment = Alignment.End)
        )
        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            AppTextButton(
                text = "Async",
                style = ButtonStyle.Green,
                isEnable = statusState.value == Task.Status.PENDING,
                onClick = { taskWorker.altStart1(task) }
            )
            AppTextButton(
                text = "Sync",
                style = ButtonStyle.Yellow,
                isEnable = statusState.value == Task.Status.PENDING,
                onClick = { taskWorker.start(task) }
            )
            AppTextButton(
                text = "Cancel",
                style = ButtonStyle.Red,
                isEnable = (statusState.value == Task.Status.ASYNC_PROGRESS) ||
                        (statusState.value == Task.Status.SYNC_PROGRESS),
                onClick = { taskWorker.cancel(task) }
            )
//            AppTextButton(
//                text = "X",
//                style = ButtonStyle.Remove,
//                isEnable = (statusState.value == BigTask.Status.CANCELLED) ||
//                        (statusState.value == BigTask.Status.FINISHED) ||
//                        (statusState.value == BigTask.Status.PENDING),
//                onClick = { taskWorker.remove(task) }
//            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    CoroutineItemAsync(AsyncTask(2), object : TaskManager {
        override val tasks: MutableList<Task>
            get() = mutableListOf()

        override fun createTask(type: Task.Type): List<Task> = listOf()
        override fun start(task: Task) {}
        override fun altStart1(task: Task) {}
        override fun altStart2(task: Task) {}
        override fun cancel(task: Task) {}
        override fun remove(task: Task): List<Task> = listOf()

    })
}