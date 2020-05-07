package org.jan

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    launch {
        delay(200L)
        Profiler.log("Launched task from runBlocking - context: $coroutineContext")
    }

    coroutineScope { // Creates a new coroutine scope
        launch {
            delay(900L)
            Profiler.log("Launch task from coroutineScope - context: $coroutineContext")
        }

        delay(100L)
        Profiler.log("Task from coroutineScope - context: $coroutineContext") // This line will be printed before nested launch
    }

    Profiler.log("runBlocking after coroutineScope ended - context: $coroutineContext") // This line is not printed until nested launch completes
}