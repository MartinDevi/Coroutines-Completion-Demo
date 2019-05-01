package com.mdevillers.coroutines.completion.demo

import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Fail a job constructed explicitly using a factory method like [Job] or [SupervisorJob] without a parent job.
 *
 * Tests will fail if the job was cancelled.
 */
class JobFailureCancellationTest {

    private val coroutineExceptionHandler
        get() = CoroutineExceptionHandler { _, _ -> }

    @Before
    fun setUp() {
        clearCoroutineExceptions()
    }

    @Test
    fun `launch + Job { throw }`() = runTest(parent = Job()) {
        launch {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `async + Job { throw }`() = runTest(parent = Job()) {
        async {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `launch + Job + coroutineExceptionHandler { throw }`() = runTest(parent = Job(), coroutineContext = coroutineExceptionHandler) {
        launch {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `async + Job + coroutineExceptionHandler { throw }`() = runTest(parent = Job(), coroutineContext = coroutineExceptionHandler) {
        async {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `launch + SupervisorJob { throw }`() = runTest(parent = SupervisorJob()) {
        launch {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `async + SupervisorJob { throw }`() = runTest(parent = SupervisorJob()) {
        async {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `launch + SupervisorJob + coroutineExceptionHandler { throw }`() = runTest(parent = SupervisorJob(), coroutineContext = coroutineExceptionHandler) {
        launch {
            throw Exception("Coroutine failed")
        }
    }

    @Test
    fun `async + SupervisorJob + coroutineExceptionHandler { throw }`() = runTest(parent = SupervisorJob(), coroutineContext = coroutineExceptionHandler) {
        async {
            throw Exception("Coroutine failed")
        }
    }

    private inline fun runTest(parent: Job, coroutineContext: CoroutineContext = EmptyCoroutineContext, crossinline block: suspend CoroutineScope.() -> Job) = runBlocking {
        CoroutineScope(parent + coroutineContext).block().join()
        assert(!parent.isCancelled)
    }
}