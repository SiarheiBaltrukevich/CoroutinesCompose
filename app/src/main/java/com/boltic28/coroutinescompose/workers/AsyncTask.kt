package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TASK_1_STEP_MILLIS = 200L
private const val TASK_2_STEP_MILLIS = 70L
private const val TASK_3_STEP_MILLIS = 130L

class AsyncTask(override val id: Int): Task() {

    override val type = Type.ASYNC

    override fun asyncStart(){
        worker = CoroutineScope(Dispatchers.Default).launch {
            setStatus(Status.ASYNC_PROGRESS)
            launch { task1() }
            launch { task2() }
            launch { task3() }
        }
    }

    override fun syncStart(){
        worker = CoroutineScope(Dispatchers.Default).launch {
            setStatus(Status.SYNC_PROGRESS)
            task1()
            task2()
            task3()
        }
    }

    private var task1Progress: Int = START_VALUE
    private var task2Progress: Int = START_VALUE
    private var task3Progress: Int = START_VALUE

    private val progress1 = MutableStateFlow(convertProgressToString(task1Progress))
    fun observeProgress1(): StateFlow<String> = progress1.asStateFlow()

    private val progress2 = MutableStateFlow(convertProgressToString(task2Progress))
    fun observeProgress2(): StateFlow<String> = progress2.asStateFlow()

    private val progress3 = MutableStateFlow(convertProgressToString(task3Progress))
    fun observeProgress3(): StateFlow<String> = progress3.asStateFlow()

    private suspend fun task1() {
        while (task1Progress < FINISHED_VALUE) {
            delay(TASK_1_STEP_MILLIS)
            progress1.emit(convertProgressToString(++task1Progress))
        }
        checkProgress()
    }

    private suspend fun task2() {
        while (task2Progress < FINISHED_VALUE) {
            delay(TASK_2_STEP_MILLIS)
            progress2.emit(convertProgressToString(++task2Progress))
        }
        checkProgress()
    }

    private suspend fun task3() {
        while (task3Progress < FINISHED_VALUE) {
            delay(TASK_3_STEP_MILLIS)
            progress3.emit(convertProgressToString(++task3Progress))
        }
        checkProgress()
    }

    private fun checkProgress() {
        val isFinished = task1Progress == FINISHED_VALUE &&
                task2Progress == FINISHED_VALUE &&
                task3Progress == FINISHED_VALUE
        if (isFinished) setStatus(Status.FINISHED)
    }
}