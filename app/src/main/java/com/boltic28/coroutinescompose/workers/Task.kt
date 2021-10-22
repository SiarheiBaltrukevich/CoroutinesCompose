package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

const val EMPTY_STRING = ""
const val START_VALUE = 0
const val FINISHED_VALUE = 60
private const val PROGRESS_ITEM = "|"

abstract class Task {

    abstract val scope: CoroutineScope
    abstract val id: Int
    abstract val type: Type

    protected var worker: Job = Job()

    private var startTime: Long = 0

    private var resultTime = MutableStateFlow(EMPTY_STRING)
    fun observeResult(): StateFlow<String> = resultTime.asStateFlow()

    private val status = MutableStateFlow(Status.PENDING)
    fun observeStatus(): StateFlow<Status> = status.asStateFlow()

    private var log = MutableStateFlow(EMPTY_STRING)
    fun observeLog(): StateFlow<String> = log.asStateFlow()

    open fun cancel() {
        CoroutineScope(worker).launch {
            log("canceling...")
            setStatus(Status.CANCELLED)
            worker.cancelAndJoin()
        }
    }

    abstract fun start(): Job

    open fun altStart1(): Job = start()

    open fun altStart2(): Job = start()

    private fun getTime(): Long = Date().time - startTime

    protected fun convertProgressToString(taskProgress: Int): String {
        val res = StringBuilder()
        for (i in START_VALUE..taskProgress) {
            if (i != START_VALUE) res.append(PROGRESS_ITEM)
        }
        return res.toString()
    }

    protected suspend fun setStatus(newStatus: Status) {
            when (newStatus) {
                Status.ASYNC_START, Status.SYNC_START, Status.LAZY_START ->
                    startTime = Date().time
                Status.IN_PROGRESS ->
                    log("status IN_PROGRESS")
                Status.FINISHED ->
                    resultTime.emit("Finished in ${Date().time - startTime} millis")
                Status.CANCELLED ->
                    resultTime.emit("Cancelled in ${Date().time - startTime} millis")
                else -> {
                }
            }
            status.emit(newStatus)
    }

    protected suspend fun log(msg: String) {
        log.emit(
            java.lang.StringBuilder()
                .append(log.value)
                .append("\n${getTime()} mill : $msg")
                .toString()
        )
    }

    companion object {
        fun create(scope: CoroutineScope, id: Int, type: Type): Task = when (type) {
            Type.ASYNC -> AsyncTask(scope, id)
            Type.AWAIT -> AwaitTask(scope, id)
            Type.LAZY -> LazyTask(scope, id)
            Type.CONT -> ContTask(scope, id)
            Type.CONTEXT -> ContextTask(scope, id)
        }
    }

    enum class Status {
        PENDING, LAZY_START, SYNC_START, ASYNC_START, IN_PROGRESS, CANCELLED, FINISHED
    }

    enum class Type(val isReady: Boolean) {
        ASYNC(true), AWAIT(true), LAZY(true), CONT(true), CONTEXT(false)
    }
}