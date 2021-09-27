package com.boltic28.coroutinescompose.workers

class LazyTask(override val id: Int) : Task() {

    override val type = Type.LAZY

    override fun syncStart() {
        TODO("Not yet implemented")
    }
}