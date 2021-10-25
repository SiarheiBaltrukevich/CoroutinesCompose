package com.boltic28.coroutinescompose.workers

import com.boltic28.coroutinescompose.elements.util.Storage
import com.boltic28.coroutinescompose.elements.util.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContextTask(
    override val scope: CoroutineScope,
    override val id: Int
) : Task() {

    override val type: Type = Type.CTX

    override fun start(): Job =
        scope.launch(Dispatchers.Unconfined + Storage.randomUser) {
            log("Coroutine is started")
            setStatus(Status.SYNC_START)
            task(this.coroutineContext[User])
        }.also { worker = it }

    private var taskProgress: Int = START_VALUE

    private val progressBar = MutableStateFlow(convertProgressToString(taskProgress))
    fun observeProgressBar(): StateFlow<String> = progressBar.asStateFlow()


    private suspend fun task(user: User?) {
        setStatus(Status.IN_PROGRESS)
        user?.let { u ->
            val taskN = Storage.nextTaskNumber
            log("Task #$taskN for ${u.name} is started")
            while (taskProgress < FINISHED_VALUE) {
                delay(100)
                progressBar.emit(convertProgressToString(++taskProgress))
            }
            u.tasks.add("Task #$taskN is completed")
            setStatus(Status.FINISHED)
            log("Task is finished")
            log( "user ${user.name} already has ${user.tasks.size} completed tasks")
        }
    }
}