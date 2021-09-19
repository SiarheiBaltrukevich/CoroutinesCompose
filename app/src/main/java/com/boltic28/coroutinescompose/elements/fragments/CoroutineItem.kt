package com.boltic28.coroutinescompose.elements.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boltic28.coroutinescompose.elements.buttons.AppTextButton
import com.boltic28.coroutinescompose.elements.buttons.ButtonStyle
import com.boltic28.coroutinescompose.workers.BigTask

@Composable
fun CoroutineItem(
    task: BigTask,
    syncAction: () -> Unit,
    asyncAction: () -> Unit,
    cancelAction: () -> Unit
) {
    val work1status = remember { task.observeProgress1() }.collectAsState("")
    val work2status = remember { task.observeProgress2() }.collectAsState("")
    val work3status = remember { task.observeProgress3() }.collectAsState("")
    val result = remember { task.observeResult() }.collectAsState("")
    val statusState = remember { task.observeStatus() }
        .collectAsState(initial = BigTask.Status.PENDING)

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Coroutine number ${task.id}",
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
                isEnable = statusState.value == BigTask.Status.PENDING
            ) {
                asyncAction.invoke()
                println("->> task ${task.id} async clicked")
            }
            AppTextButton(
                text = "Sync",
                style = ButtonStyle.Yellow,
                isEnable = statusState.value == BigTask.Status.PENDING
            ) {
                syncAction.invoke()
                println("->> task ${task.id} sync clicked")
            }
            AppTextButton(
                text = "Cancel",
                style = ButtonStyle.Red,
                isEnable = (statusState.value == BigTask.Status.ASYNC_PROGRESS) ||
                        (statusState.value == BigTask.Status.SYNC_PROGRESS)
            ) {
                cancelAction.invoke()
                println("->> task ${task.id} cancel clicked")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun preview() {
    CoroutineItem(BigTask(2), {}, {}, {})
}