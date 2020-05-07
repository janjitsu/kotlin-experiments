package org.jan

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

suspend fun main(args: Array<String>) {
    println("Coroutines!")
    Profiler.log("Start execution")

// Start a coroutine
    GlobalScope.launch {
        Profiler.log("Global Scope launch coroutine delay 1 second")
        delay(1000)
        Profiler.log("Hello")
    }

    Profiler.log("suspend main function delay 2 seconds coroutine")
    delay(2000)

    //Thread.sleep(2000) // wait for 2 seconds
    Profiler.log("Stop execution")
}