package com.mdevillers.coroutines.completion.demo

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Fail a scope built using a builder like [coroutineScope] or [supervisorScope].
 *
 * Tests will throw an [AssertionError] if the uncaught exception handler was invoked.
 */
class ScopeFailureUncaughtExceptionHandlerTest {

    @Test
    fun `coroutineScope { throw }`() = runTest {
        coroutineScope {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `coroutineScope { launch { throw } }`() = runTest {
        coroutineScope {
            launch {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `coroutineScope { async { throw } }`() = runTest {
        coroutineScope {
            async {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `coroutineScope { Job & parent completeExceptionally }`() = runTest {
        coroutineScope {
            Job(coroutineContext[Job]).completeExceptionally(Exception("Coroutine failed"))
        }
    }

    @Test
    fun `supervisorScope { throw }`() = runTest {
        supervisorScope {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `supervisorScope { launch { throw } }`() = runTest {
        supervisorScope {
            launch {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `supervisorScope { async { throw } }`() = runTest {
        supervisorScope {
            async {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `supervisorScope { Job & parent completeExceptionally }`() = runTest {
        supervisorScope {
            Job(parent = coroutineContext[Job]).completeExceptionally(Exception("Coroutine failed"))
        }
    }

    private inline fun runTest(crossinline block: suspend () -> Unit) = runBlocking {
        runCatching { block() }
        throwCoroutineExceptions()
    }
}