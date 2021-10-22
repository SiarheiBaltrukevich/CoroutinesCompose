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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.boltic28.coroutinescompose.elements.buttons.AppTextButton
import com.boltic28.coroutinescompose.elements.buttons.ButtonStyle
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemAwait
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemAsync
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemCont
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItemLazy
import com.boltic28.coroutinescompose.ui.theme.CoroutinesComposeTheme
import com.boltic28.coroutinescompose.workers.*
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinesComposeTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val coroutines: SnapshotStateList<Task> = remember { mutableStateListOf() }
                    coroutines.swapList(viewModel.tasks)

                    val scopeStatus = remember {viewModel.scopeStatus}
                        .collectAsState(initial = MainViewModel.ScopeStatus.READY)

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            coroutines.forEach { task ->
                                item {
                                    when (task) {
                                        is AsyncTask -> CoroutineItemAsync(task, viewModel)
                                        is AwaitTask -> CoroutineItemAwait(task, viewModel)
                                        is LazyTask -> CoroutineItemLazy(task, viewModel)
                                        is ContTask -> CoroutineItemCont(task, viewModel)
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
                        if (scopeStatus.value == MainViewModel.ScopeStatus.READY) {
                            AppTextButton(
                                text = "Cancel ALL",
                                style = ButtonStyle.MaxWidthRed,
                                isEnable = coroutines.isNotEmpty()
                            ) {
                                viewModel.cancelAll("User finish all the coroutines")
                            }
                        } else {
                            AppTextButton(
                                text = "Restart SCOPE",
                                style = ButtonStyle.MaxWidthBlue,
                                isEnable = coroutines.isNotEmpty()
                            ) {
                                viewModel.restartScope()
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
