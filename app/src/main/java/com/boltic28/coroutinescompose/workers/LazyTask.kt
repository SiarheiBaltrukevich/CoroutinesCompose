package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LazyTask(
    override val scope: CoroutineScope,
    override val id: Int
) : Task() {

    override val type = Type.LAZY

    private var taskProgress: Int = START_VALUE

    private val progressBar = MutableStateFlow(convertProgressToString(taskProgress))
    fun observeProgressBar(): StateFlow<String> = progressBar.asStateFlow()

    override fun start(): Job =
        scope.launch(start = CoroutineStart.LAZY) {
            task()
        }.also {
            scope.launch {
                setStatus(Status.LAZY_START)
                worker = it
                log("Ready to start.")
                log("isCoroutineActive: ${it.isActive}")
            }
        }

    override fun altStart1(): Job =
        worker.also {
            scope.launch {
                log("Started")
                setStatus(Status.IN_PROGRESS)
                worker.start()
                log("isCoroutineActive: ${it.isActive}")
            }
        }

    private suspend fun task() {
        log("Task is started")
        while (taskProgress < FINISHED_VALUE) {
            delay(200)
            progressBar.emit(convertProgressToString(++taskProgress))
        }
        setStatus(Status.FINISHED)
        log("Task is finished")
    }
}