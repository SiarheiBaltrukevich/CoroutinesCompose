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
import com.boltic28.coroutinescompose.workers.ContextTask
import com.boltic28.coroutinescompose.workers.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun CoroutineItemContext(task: ContextTask, taskWorker: TaskManager) {
    val log = remember { task.observeLog() }.collectAsState("")
    val progress = remember { task.observeProgressBar() }.collectAsState("")
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
            text = "Context Object Task #${task.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(text = "work status: ${progress.value}")
        Text(text = "Log: ${log.value}")
        Text(
            text = "Coroutine status: ${statusState.value}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = result.value,
            modifier = Modifier.align(alignment = Alignment.End)
        )
        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            AppTextButton(
                text = "START",
                style = ButtonStyle.Green,
                isEnable = statusState.value == Task.Status.PENDING,
                onClick = { taskWorker.start(task) }
            )
            AppTextButton(
                text = "CANCEL",
                style = ButtonStyle.Red,
                isEnable = (statusState.value == Task.Status.SYNC_START) ||
                        (statusState.value == Task.Status.ASYNC_START) ||
                        (statusState.value == Task.Status.LAZY_START) ||
                        (statusState.value == Task.Status.IN_PROGRESS),
                onClick = { taskWorker.cancel(task) }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewContext() {
    CoroutineItemContext(ContextTask(CoroutineScope(EmptyCoroutineContext),2), object : TaskManager {
        override val tasks: MutableList<Task>
            get() = mutableListOf()

        override fun createTask(type: Task.Type): List<Task> = listOf()
        override fun start(task: Task): Job = Job()
        override fun altStart1(task: Task): Job = Job()
        override fun altStart2(task: Task): Job = Job()
        override fun cancel(task: Task) {}
        override fun cancelAll(msg: String) {}
        override fun restartScope() {}
        override fun remove(task: Task): List<Task> = listOf()

    })
}