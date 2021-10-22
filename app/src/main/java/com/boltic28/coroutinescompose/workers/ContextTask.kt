package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class ContextTask(
    override val scope: CoroutineScope,
    override val id: Int
) : Task() {

    private val user = User(23, "Artur", 67)

    override val type: Type = Type.CONTEXT

    override fun start(): Job =
        scope.launch(Dispatchers.Unconfined + user) {

        }

    fun cancelEx1() {

        val user = User(1, "Alex", 56)
        val context = Job() + Dispatchers.Main + user
        val scope = CoroutineScope(context)

        val user2 = User(3, "Mike", 23)
        scope.launch {
            val userFromContext = this.coroutineContext[User]

            launch(Dispatchers.IO + user2) { }
        }

        val job = scope.async(start = CoroutineStart.LAZY) { delay(30000) }
        val job4 = scope.launch(start = CoroutineStart.LAZY) { }

        scope.launch {
            val result = job.await()

            job4.start()
            job4.join()
        }

        val job1 = scope.launch { delay(10000) }

        val job2 = scope.async(start = CoroutineStart.LAZY) { delay(20000) }
        val job3 = scope.launch { delay(20000) }
        scope.cancel()
        scope.cancel("user closed the page")
        scope.cancel(CancellationException("child coroutine error", Exception()))

        job.isActive; job.isCancelled; job.isCompleted

    }
}

data class User(
    val id: Long,
    val name: String,
    val age: Int
) : AbstractCoroutineContextElement(User) {
    companion object Key : CoroutineContext.Key<User>
}