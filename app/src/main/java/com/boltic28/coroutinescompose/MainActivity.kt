package com.boltic28.coroutinescompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.boltic28.coroutinescompose.elements.buttons.AppTextButton
import com.boltic28.coroutinescompose.elements.buttons.ButtonStyle
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemAwait
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemAsync
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemLazy
import com.boltic28.coroutinescompose.ui.theme.CoroutinesComposeTheme
import com.boltic28.coroutinescompose.workers.AsyncTask
import com.boltic28.coroutinescompose.workers.AwaitTask
import com.boltic28.coroutinescompose.workers.Task
import com.boltic28.coroutinescompose.workers.LazyTask

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinesComposeTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val coroutines: SnapshotStateList<Task> = remember { mutableStateListOf() }
                    coroutines.swapList(viewModel.tasks)

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            coroutines.forEach { task ->
                                item {
                                    when (task.type) {
                                        Task.Type.ASYNC -> CoroutineItemAsync(task as AsyncTask, viewModel)
                                        Task.Type.AWAIT -> CoroutineItemAwait(task as AwaitTask, viewModel)
                                        Task.Type.LAZY -> CoroutineItemLazy(task as LazyTask, viewModel)
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                        ) {
                            Task.Type.values().forEach { type ->
                                AppTextButton(
                                    text = type.name,
                                    style = ButtonStyle.Blue,
                                    isEnable = type.isReady
                                ) {
                                    coroutines.swapList(viewModel.createTask(type))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}
