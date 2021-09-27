package com.boltic28.coroutinescompose.workers

class ContinuationTask(override val id: Int) : Task() {

    override val type: Type = Type.LAZY

    override fun syncStart() {
        TODO("Not yet implemented")
    }

//    fun continuationStart(){
//        worker = CoroutineScope(Dispatchers.Default).launch {
//            setStatus(Status.SYNC_PROGRESS)
//            task1()
//            task2()
//            task3()
//        }
//    }
}