package org.jan

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    Profiler.log("Launch 1 Million Coroutines")

    val c = AtomicLong()

    for (i in 1..1_000_000L)
        GlobalScope.launch {
            c.addAndGet(i)
        }

    Profiler.log("launch finishes faster but does not synchronize")
    Profiler.log("Total ${c.get()}")
    Profiler.log("-----------------------------------------------")

    Profiler.log("Launch 1 Million Coroutines with async and await")
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            delay(1000) //even with delay coroutines run in parallel
            n
        }
    }
    runBlocking {
        val sum = deferred.map { it.await().toLong() }.sum()
        Profiler.log("Await finishes as slower as threads but with correct values")
        Profiler.log("Total $sum")
    }

    Profiler.log("-----------------------------------------------")
    Profiler.log("Start 1 Million Threads")
    val d = AtomicLong()

    for (j in 1..1_000_000L)
        thread(start = true) {
            d.addAndGet(j)
        }

    Profiler.log("Total ${d.get()}")
}