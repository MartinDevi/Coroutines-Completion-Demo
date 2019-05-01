package com.mdevillers.coroutines.completion.demo

import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test

/**
 * Fail a coroutine built using a builder like [launch] or [async] without a parent job.
 *
 * Tests will fail if the uncaught exception handler was invoked.
 */
class BuilderFailureUncaughtExceptionHandlerTest {

    private val coroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, _ -> }

    @Before
    fun setUp() {
        clearCoroutineExceptions()
    }

    @Test
    fun `launch { throw }`() = runTest {
        GlobalScope.launch {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `async { throw }`() = runTest {
        GlobalScope.async {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `launch { launch { throw } }`() = runTest {
        GlobalScope.launch {
            launch {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `launch { async { throw } }`() = runTest {
        GlobalScope.launch {
            async {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `async { launch { throw } }`() = runTest {
        GlobalScope.async {
            launch {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `async { async { throw } }`() = runTest {
        GlobalScope.async {
            async {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `launch + coroutineExceptionHandler { throw }`() = runTest {
        GlobalScope.launch(coroutineExceptionHandler) {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `launch { launch + coroutineExceptionHandler { throw } }`() = runTest {
        GlobalScope.launch {
            launch(coroutineExceptionHandler) {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `launch { async + coroutineExceptionHandler { throw } }`() = runTest {
        GlobalScope.launch {
            async(coroutineExceptionHandler) {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `async + coroutineExceptionHandler { throw }`() = runTest {
        GlobalScope.async(coroutineExceptionHandler) {
            throw Exception("Coroutine failed")
        }
    }


    @Test
    fun `async { launch + coroutineExceptionHandler { throw } }`() = runTest {
        GlobalScope.async {
            launch(coroutineExceptionHandler) {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `async { async + coroutineExceptionHandler { throw } }`() = runTest {
        GlobalScope.async {
            async(coroutineExceptionHandler) {
                throw Exception("Coroutine failed")
            }
        }
    }

    @Test
    fun `launch { Job & parent completeExceptionally }`() = runTest {
        GlobalScope.launch {
            Job(coroutineContext[Job]).completeExceptionally(Exception("Coroutine failed"))
        }
    }

    @Test
    fun `async { Job & parent completeExceptionally }`() = runTest {
        GlobalScope.async {
            Job(coroutineContext[Job]).completeExceptionally(Exception("Coroutine failed"))
        }
    }

    private inline fun runTest(crossinline block: () -> Job) = runBlocking {
        block().join()
        throwCoroutineExceptions()
    }
}