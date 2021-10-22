package com.boltic28.coroutinescompose

import androidx.lifecycle.ViewModel
import com.boltic28.coroutinescompose.workers.Task
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.AbstractCoroutineContextKey
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MainViewModel : ViewModel(), TaskManager {

    enum class ScopeStatus {
        READY, CANCELLED
    }

    private var taskCounter: Int = 0

    private var scope = CoroutineScope(Dispatchers.Default)

    private val status = MutableStateFlow(ScopeStatus.READY)
    val scopeStatus = status.asStateFlow()

    override val tasks: MutableList<Task> = mutableListOf()

    override fun createTask(type: Task.Type): List<Task> {
        tasks.add(Task.create(scope, taskCounter++, type))
        return tasks
    }

    override fun altStart1(task: Task): Job =
        task.altStart1()

    override fun altStart2(task: Task): Job =
        task.altStart2()

    override fun start(task: Task): Job =
        task.start()

    override fun cancel(task: Task) {
        task.cancel()
    }

    override fun cancelAll(msg: String) {
        CoroutineScope(Dispatchers.Main).launch {
            scope.cancel(msg)
            status.emit(ScopeStatus.CANCELLED)
        }
    }

    override fun remove(task: Task): List<Task> {
        tasks.remove(task)
        return tasks
    }

    override fun restartScope() {
        CoroutineScope(Dispatchers.Main).launch {
            scope = CoroutineScope(Dispatchers.Default)
            status.emit(ScopeStatus.READY)
        }
    }
}