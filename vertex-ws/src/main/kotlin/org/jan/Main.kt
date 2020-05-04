package org.jan

import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait

suspend fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticleAwait(ServerVerticle::class.java.name)
}