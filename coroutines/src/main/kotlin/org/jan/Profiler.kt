package org.jan

object Profiler {

    private val start = System.currentTimeMillis()

    fun log(text: String): Unit = println("[L${ln()}:T${time()}ms:${threadId()}Thread]\t $text")

    private fun ln(): Int = Exception().getStackTrace()[2].getLineNumber()

    private fun threadId(): String? = Thread.currentThread().getName()

    private fun time(): Long = (System.currentTimeMillis() - start)
}