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
import com.boltic28.coroutinescompose.workers.ContTask
import com.boltic28.coroutinescompose.workers.Task

@Composable
fun CoroutineItemCont(task: ContTask, taskWorker: TaskManager) {
    val report = remember { task.observeReport() }.collectAsState("")
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
            text = "Continuation test, task #${task.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(text = "Log: ${report.value}")
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
                text = "START C",
                style = ButtonStyle.Green,
                isEnable = statusState.value == Task.Status.PENDING,
                onClick = { taskWorker.start(task) }
            )
            AppTextButton(
                text = "START R",
                style = ButtonStyle.Green,
                isEnable = statusState.value == Task.Status.PENDING,
                onClick = { taskWorker.altStart1(task) }
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
fun PreviewCont() {
    CoroutineItemCont(ContTask(2), object : TaskManager {
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