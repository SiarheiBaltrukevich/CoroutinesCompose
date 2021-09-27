package com.boltic28.coroutinescompose

import com.boltic28.coroutinescompose.workers.Task

interface TaskManager {

    val tasks: MutableList<Task>

    fun createTask(type: Task.Type): List<Task>
    fun asyncStart(task: Task)
    fun syncStart(task: Task)
    fun cancel(task: Task)
    fun remove(task: Task): List<Task>
}