package org.jan

import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.await
import io.vertx.tracing.opentracing.OpenTracingOptions
import org.jan.repository.UserAccountRepositoryVerticle


suspend fun main() {
    val vertx = Vertx.vertx(VertxOptions().setTracingOptions(OpenTracingOptions()))
    val retriever = ConfigRetriever.create(
        vertx,
        configRetrieverOptionsOf(
            stores = listOf(
                configStoreOptionsOf(
                    type = "file",
                    config = jsonObjectOf(
                        "path" to "app.json"
                    )
                )
            )
        )
    )

    vertx.deployVerticle(
        ServerVerticle::class.java.name, deploymentOptionsOf(
            config = retriever.config.await()
        )
    ).await()
    vertx.deployVerticle(
        UserAccountRepositoryVerticle::class.java.name,
        deploymentOptionsOf(
            config = retriever.config.await()
        )
    ).await()
}