package com.boltic28.coroutinescompose

import androidx.lifecycle.ViewModel
import com.boltic28.coroutinescompose.workers.Task

class MainViewModel: ViewModel(), TaskManager {

    private var taskCounter: Int = 0

    override val tasks: MutableList<Task> = mutableListOf()

    override fun createTask(type: Task.Type): List<Task>{
        tasks.add(Task.create(taskCounter++, type))
        return tasks
    }

    override fun asyncStart(task: Task){
        task.asyncStart()
    }

    override fun syncStart(task: Task){
        task.syncStart()
    }

    override fun cancel(task: Task){
        task.cancel()
    }

    override fun remove(task: Task): List<Task>{
        tasks.remove(task)
        return tasks
    }
}