package com.boltic28.coroutinescompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boltic28.coroutinescompose.workers.BigTask
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var taskCounter: Int = 0

    val tasks: MutableList<BigTask> = mutableListOf()

    fun asyncStart(task: BigTask){
        task.worker = viewModelScope.launch {
            task.setStatus(BigTask.Status.ASYNC_PROGRESS)
            launch { task.task1() }
            launch { task.task2() }
            launch { task.task3() }
        }
    }

    fun syncStart(task: BigTask){
        task.worker = viewModelScope.launch {
            task.setStatus(BigTask.Status.SYNC_PROGRESS)
            task.task1()
            task.task2()
            task.task3()
        }
    }

    fun startWithResult(task: BigTask){

    }

    fun cancel(task: BigTask){
        viewModelScope.launch {
            task.setStatus(BigTask.Status.CANCELLED)
            task.worker.cancelAndJoin()
        }
    }

    fun createTask(): BigTask = BigTask(taskCounter++).also {
        tasks.add(it)
    }
}