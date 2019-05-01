package com.mdevillers.coroutines.completion.demo

import kotlinx.coroutines.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private val coroutineExceptions = mutableListOf<Throwable>()

fun clearCoroutineExceptions() {
    coroutineExceptions.clear()
}

fun throwCoroutineExceptions() {
    val exceptions = consumeExceptions()
    if (exceptions.isNotEmpty()) {
        throw AssertionError().apply {
            exceptions.forEach { exception ->
                addSuppressed(exception)
            }
        }
    }
}

private fun consumeExceptions() =
    coroutineExceptions.toList().also { coroutineExceptions.clear() }

class TestCoroutineExceptionHandler: AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        coroutineExceptions += exception
    }

}
