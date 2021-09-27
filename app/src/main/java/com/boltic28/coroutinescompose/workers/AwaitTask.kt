package com.boltic28.coroutinescompose.workers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val DEF_STEP_MILLIS = 50L

class AwaitTask(override val id: Int) : Task() {

    override val type = Type.AWAIT

    override fun syncStart() {
        worker = CoroutineScope(Dispatchers.Default).launch {
            setStatus(Status.SYNC_PROGRESS)
            setReport("waiting for def result")

            val report: Deferred<String> = async { waitResult() }

            // code will continue after awaiting the result
            setReport(report.await())
            setStatus(Status.FINISHED)
        }
    }

    private var defProgress: Int = START_VALUE

    private var report = MutableStateFlow("wait for result")
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
        return "deffered result is READY"
    }
}