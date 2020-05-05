package org.jan

import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import org.jan.repository.UserAccountRepositoryVerticle

suspend fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticleAwait(ServerVerticle::class.java.name)
    vertx.deployVerticleAwait(UserAccountRepositoryVerticle::class.java.name)
}