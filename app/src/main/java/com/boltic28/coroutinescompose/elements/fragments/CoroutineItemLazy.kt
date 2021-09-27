package com.boltic28.coroutinescompose.elements.fragments

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.boltic28.coroutinescompose.TaskManager
import com.boltic28.coroutinescompose.workers.Task
import com.boltic28.coroutinescompose.workers.LazyTask

@Composable
fun CoroutineItemLazy(task: LazyTask, taskWorker: TaskManager) {
//    val report = remember { task.observeReport() }.collectAsState("")
//    val progress = remember { task.observeProgress1() }.collectAsState("")
//    val result = remember { task.observeResult() }.collectAsState("")
//    val statusState = remember { task.observeStatus() }
//        .collectAsState(initial = BigTask.Status.PENDING)
//
//    Column(
//        horizontalAlignment = Alignment.Start,
//        verticalArrangement = Arrangement.Top,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Await test task #${task.id}",
//            fontWeight = FontWeight.Bold,
//            fontSize = 24.sp,
//            modifier = Modifier
//                .align(alignment = Alignment.CenterHorizontally)
//        )
//        Text(text = "Report: ${report.value}")
//        Text(text = "work 1 status: ${progress.value}")
//        Text(
//            text = "Coroutine status: ${statusState.value}",
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
//        )
//        Text(
//            text = result.value,
//            modifier = Modifier.align(alignment = Alignment.End)
//        )
//        Row(
//            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
//        ) {
//            AppTextButton(
//                text = "START",
//                style = ButtonStyle.Green,
//                isEnable = statusState.value == BigTask.Status.PENDING,
//                onClick = { taskWorker.defStart(task) }
//            )
//            AppTextButton(
//                text = "Cancel",
//                style = ButtonStyle.Red,
//                isEnable = (statusState.value == BigTask.Status.ASYNC_PROGRESS) ||
//                        (statusState.value == BigTask.Status.SYNC_PROGRESS),
//                onClick = { taskWorker.cancel(task) }
//            )
//            AppTextButton(
//                text = "X",
//                style = ButtonStyle.Remove,
//                isEnable = (statusState.value == BigTask.Status.CANCELLED) ||
//                        (statusState.value == BigTask.Status.FINISHED) ||
//                        (statusState.value == BigTask.Status.PENDING),
//                onClick = { taskWorker.remove(task) }
//            )
//        }
//    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLazy() {
    CoroutineItemLazy(LazyTask(2), object : TaskManager {
        override val tasks: MutableList<Task>
            get() = mutableListOf()

        override fun createTask(type: Task.Type): List<Task> = listOf()
        override fun asyncStart(task: Task) {}
        override fun syncStart(task: Task) {}
        override fun cancel(task: Task) {}
        override fun remove(task: Task): List<Task> = listOf()

    })
}