package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val DEF_STEP_MILLIS = 50L

class AwaitTask(
    override val scope: CoroutineScope,
    override val id: Int
) : Task() {

    override val type = Type.AWAIT

    override fun start() = scope.launch {
        log("Sync is started")
        setStatus(Status.SYNC_START)
        setStatus(Status.IN_PROGRESS)
        setReport("waiting for def result")

        val report: Deferred<String> = async {
            log("Async is started")
            waitResult()
        }

        // code will continue after awaiting the result
        setReport(report.await())
        log("result is used")
        setStatus(Status.FINISHED)
    }.also { worker = it }

    override fun cancel() {
        CoroutineScope(worker).launch {
            setReport("coroutine was canceled. No result.")
            //call coroutine inside another one
            super.cancel()
        }
    }

    private var defProgress: Int = START_VALUE

    private var report = MutableStateFlow("loading....")
    fun observeReport(): StateFlow<String> = report.asStateFlow()

    private val progressBar = MutableStateFlow(convertProgressToString(defProgress))
    fun observeProgressBar(): StateFlow<String> = progressBar.asStateFlow()

    private fun setReport(reportMessage: String) {
        CoroutineScope(worker).launch {
            report.emit(reportMessage)
        }
    }

    private suspend fun waitResult(): String {
        while (defProgress < FINISHED_VALUE) {
            delay(DEF_STEP_MILLIS)
            progressBar.emit(convertProgressToString(++defProgress))
        }
        log("result is ready")
        return "deffered result is READY"
    }
}