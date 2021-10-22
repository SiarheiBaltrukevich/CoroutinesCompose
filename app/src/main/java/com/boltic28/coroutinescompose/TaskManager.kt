package com.boltic28.coroutinescompose

import com.boltic28.coroutinescompose.workers.Task
import kotlinx.coroutines.Job

interface TaskManager {

    val tasks: MutableList<Task>

    fun createTask(type: Task.Type): List<Task>

    fun start(task: Task): Job
    fun altStart1(task: Task): Job
    fun altStart2(task: Task): Job

    fun cancel(task: Task)
    fun cancelAll(msg: String = "")
    fun remove(task: Task): List<Task>

    fun restartScope()
}