package com.boltic28.coroutinescompose.elements.util

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

object Storage {

    private var counter = 0
    val nextTaskNumber: Int
        get() = ++counter

    val size: Int
        get() = users.size

    var users = mutableListOf(
        User(1, "Alex", 22),
        User(2, "jack", 32),
        User(3, "Olga", 24),
        User(4, "Mike", 26),
        User(5, "John", 42)
    )

    val randomUser: User
        get() = users.random()

}

data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val tasks: MutableList<String> = mutableListOf()
) : AbstractCoroutineContextElement(User) {
    companion object Key : CoroutineContext.Key<User>
}