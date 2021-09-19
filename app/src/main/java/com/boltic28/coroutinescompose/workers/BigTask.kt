package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val START_VALUE = 0
private const val FINISHED_VALUE = 60
private const val TASK_1_STEP_MILLIS = 200L
private const val TASK_2_STEP_MILLIS = 70L
private const val TASK_3_STEP_MILLIS = 130L

class BigTask(val id: Int) {

    var worker: Job = Job()

    private var startTime: Long = 0


    private var task1Progress: Int = START_VALUE
    private var task2Progress: Int = START_VALUE
    private var task3Progress: Int = START_VALUE

    private var resultTime = MutableStateFlow("")
    fun observeResult(): StateFlow<String> = resultTime.asStateFlow()

    private val status = MutableStateFlow(Status.PENDING)
    fun observeStatus(): StateFlow<Status> = status.asStateFlow()

    private val progress1 = MutableStateFlow(convertProgressToString(task1Progress))
    fun observeProgress1(): StateFlow<String> = progress1.asStateFlow()

    private val progress2 = MutableStateFlow(convertProgressToString(task2Progress))
    fun observeProgress2(): StateFlow<String> = progress2.asStateFlow()

    private val progress3 = MutableStateFlow(convertProgressToString(task3Progress))
    fun observeProgress3(): StateFlow<String> = progress3.asStateFlow()

    private fun convertProgressToString(taskProgress: Int): String {
        val res = StringBuilder()
        for (i in START_VALUE..taskProgress) {
            if (i != START_VALUE) res.append("|")
        }
        return res.toString()
    }

    private fun checkProgress() {
        val isFinished = task1Progress == FINISHED_VALUE &&
                task2Progress == FINISHED_VALUE &&
                task3Progress == FINISHED_VALUE
        if (isFinished) setStatus(Status.FINISHED)
    }

    fun setStatus(newStatus: Status) {
        CoroutineScope(worker).launch {
            when (newStatus) {
                Status.ASYNC_PROGRESS, Status.SYNC_PROGRESS ->
                    startTime = Date().time
                Status.FINISHED ->
                    resultTime.emit("Finished in ${Date().time - startTime} millis")
                Status.CANCELLED ->
                    resultTime.emit("Cancelled in ${Date().time - startTime} millis")
                else -> {}
            }
            status.emit(newStatus)
        }
    }

    suspend fun task1() {
        while (task1Progress < FINISHED_VALUE) {
            delay(TASK_1_STEP_MILLIS)
            progress1.emit(convertProgressToString(++task1Progress))
        }
        checkProgress()
    }

    suspend fun task2() {
        while (task2Progress < FINISHED_VALUE) {
            delay(TASK_2_STEP_MILLIS)
            progress2.emit(convertProgressToString(++task2Progress))
        }
        checkProgress()
    }

    suspend fun task3() {
        while (task3Progress < FINISHED_VALUE) {
            delay(TASK_3_STEP_MILLIS)
            progress3.emit(convertProgressToString(++task3Progress))
        }
        checkProgress()
    }

    enum class Status(value: String) {
        PENDING("pending"),
        SYNC_PROGRESS("sync started"),
        ASYNC_PROGRESS("async started"),
        CANCELLED("canceled"),
        FINISHED("finished")
    }
}