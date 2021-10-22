package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val STEP_MILLIS = 1000L
private const val TASK_1_RESULT = "task 1 finished"
private const val ADDITIONAL_RESULT = "additional res"


class ContTask(
    override val scope: CoroutineScope,
    override val id: Int
) : Task() {

    override val type: Type = Type.LAZY

    override fun start(): Job = start(true)

    override fun altStart1(): Job = start(false)

    private fun start(useContinuation: Boolean): Job =
        scope.launch {
            setStatus(Status.SYNC_START)
            setStatus(Status.IN_PROGRESS)
            log("isContinuationUse: $useContinuation")
            delay(STEP_MILLIS)

            val res1 = task1(useContinuation)
            val res2 = task2(res1, useContinuation)
            showResult(res2)
        }.also { worker = it }

    private suspend fun task1(useContinuation: Boolean = false): String {
        log("task 1 is started.")

        for (i in 0..2) {
            delay(STEP_MILLIS)
        }

        return if (useContinuation) {
            //return the result of work in coroutines thread
            suspendCoroutine {
                it.resume(TASK_1_RESULT)
            }
        } else {
            TASK_1_RESULT
        }
    }

    private suspend fun task2(res1: String, useContinuation: Boolean = false): Boolean {
        log("task 2 is started.Res1 is: ${res1 == TASK_1_RESULT}")

        val flag = additionalTask(useContinuation) == ADDITIONAL_RESULT

        return if (useContinuation) {
            //return the result of work in coroutines thread
            suspendCoroutine {
                it.resume(flag)
            }
        } else {
            flag
        }
    }

    private suspend fun additionalTask(useContinuation: Boolean = false): String {
        log("additional task is started")
        return if (useContinuation) {
            //return the result of work in coroutines thread
            suspendCoroutine { continuation ->
                kotlin.run {
                    try {
                        thread {
                            for (i in 0..4) {
                                Thread.sleep(1000)
                            }
                            continuation.resume(ADDITIONAL_RESULT)
                        }
                    }catch (e: Exception){
                        continuation.resumeWithException(e)
                    }
                }
            }
        } else {
            kotlin.run {
                try {
                    for (i in 0..4){
                        Thread.sleep(1000)
                    }
                    ADDITIONAL_RESULT
                }catch (e: Exception){
                    "error"
                }
            }
        }
    }

    private suspend fun showResult(isSuccess: Boolean) {
        if (isSuccess) {
            log("work is finished")
        } else {
            log("work is failed")
        }
        setStatus(Status.FINISHED)
    }
}