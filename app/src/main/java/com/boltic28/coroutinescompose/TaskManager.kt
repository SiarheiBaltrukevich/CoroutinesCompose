package com.boltic28.coroutinescompose

import com.boltic28.coroutinescompose.workers.Task

interface TaskManager {

    val tasks: MutableList<Task>

    fun createTask(type: Task.Type): List<Task>

    fun start(task: Task)
    fun altStart1(task: Task)
    fun altStart2(task: Task)

    fun cancel(task: Task)
    fun remove(task: Task): List<Task>
}