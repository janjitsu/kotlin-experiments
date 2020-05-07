package org.jan.controller

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jan.repository.UserAccountRepositoryVerticle

object UserAccountController {

    val insertUser = Handler<RoutingContext> { routingContext ->
        GlobalScope.launch(routingContext.vertx().dispatcher()) {
            val response = routingContext.response()
                    .putHeader("content-type", "application/json")
                    .setChunked(true)

            try {
                val userAccount: Message<JsonObject> = routingContext.vertx().eventBus().requestAwait(
                        UserAccountRepositoryVerticle.USER_ACCOUNT_REPOSITORY_INSERT,
                        routingContext.bodyAsJson
                )

                println(Thread.currentThread().getName())
                response
                        .setStatusCode(201)
                        .setStatusMessage("Created")
                        .write(Json.encodePrettily(userAccount.body()))
                        .end()
            } catch(e: Exception) {
                response
                        .setStatusCode(400)
                        .setStatusMessage("Bad Request")
                        .write(
                                jsonObjectOf(
                                    "message" to e.message
                                ).toBuffer()
                        )
                        .end()
            }
        }
    }
}