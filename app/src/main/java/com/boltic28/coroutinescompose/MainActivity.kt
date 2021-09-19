package com.boltic28.coroutinescompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.boltic28.coroutinescompose.elements.buttons.AppTextButton
import com.boltic28.coroutinescompose.elements.buttons.ButtonStyle
import com.boltic28.coroutinescompose.elements.fragments.CoroutineItem
import com.boltic28.coroutinescompose.ui.theme.CoroutinesComposeTheme
import com.boltic28.coroutinescompose.workers.BigTask
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinesComposeTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val coroutines = remember { mutableStateOf(viewModel.tasks.toList()) }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f,true)
                        ){
                            coroutines.value.forEach{ task ->
                                item{
                                    CoroutineItem(
                                        task = task,
                                        syncAction = {viewModel.syncStart(task)},
                                        asyncAction = {viewModel.asyncStart(task)},
                                        cancelAction = {viewModel.cancel(task)}
                                    )
                                }
                            }
                        }
                        AppTextButton(
                            text = "ADD NEW COROUTINE",
                            style = ButtonStyle.MaxWidth,
                                    isEnable = true) {
                            viewModel.createTask()
                            coroutines.value = viewModel.tasks.toList()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutinesComposeTheme {
        CoroutineItem(BigTask(44), {}, {}, {})
    }
}