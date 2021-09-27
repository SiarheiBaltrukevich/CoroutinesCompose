package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

const val START_VALUE = 0
const val FINISHED_VALUE = 60

abstract class Task {

    companion object {
        fun create(id: Int, type: Type): Task = when(type){
                Type.ASYNC -> AsyncTask(id)
                Type.AWAIT -> AwaitTask(id)
                Type.LAZY -> LazyTask(id)
            }
    }

    abstract val id: Int
    abstract val type: Type

    protected var worker: Job = Job()

    private var startTime: Long = 0

    private var resultTime = MutableStateFlow("")
    fun observeResult(): StateFlow<String> = resultTime.asStateFlow()

    private val status = MutableStateFlow(Status.PENDING)
    fun observeStatus(): StateFlow<Status> = status.asStateFlow()

    fun cancel(){
        CoroutineScope(worker).launch {
            setStatus(Status.CANCELLED)
            worker.cancelAndJoin()
        }
    }

    abstract fun syncStart()

    open fun asyncStart() { syncStart() }

    protected fun convertProgressToString(taskProgress: Int): String {
        val res = StringBuilder()
        for (i in START_VALUE..taskProgress) {
            if (i != START_VALUE) res.append("|")
        }
        return res.toString()
    }

    protected fun setStatus(newStatus: Status) {
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

    enum class Status {
        PENDING, SYNC_PROGRESS, ASYNC_PROGRESS, CANCELLED, FINISHED
    }

    enum class Type(val isReady: Boolean) {
        ASYNC(true), AWAIT(true), LAZY(false)
    }
}