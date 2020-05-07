package org.jan

import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.core.json.jsonObjectOf
import org.jan.repository.UserAccountRepositoryVerticle


suspend fun main() {
    val vertx = Vertx.vertx()
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


    vertx.deployVerticleAwait(
        ServerVerticle::class.java.name, deploymentOptionsOf(
            config = retriever.getConfigAwait()
        )
    )
    vertx.deployVerticleAwait(
        UserAccountRepositoryVerticle::class.java.name,
        deploymentOptionsOf(
            config = retriever.getConfigAwait()
        )
    )
}