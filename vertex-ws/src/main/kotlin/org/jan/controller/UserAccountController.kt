package org.jan.controller

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.jan.repository.UserAccountRepositoryVerticle

object UserAccountController {

    val insertUser = Handler<RoutingContext> { routingContext ->

        val response = routingContext.response()
            .putHeader("content-type", "application/json")
            .setChunked(true)


        routingContext.vertx().eventBus().request(
            UserAccountRepositoryVerticle.USER_ACCOUNT_REPOSITORY_INSERT,
            routingContext.bodyAsJson,
            Handler<AsyncResult<Message<JsonObject>>> { asyncRes ->
                if (asyncRes.succeeded()) {
                    val userAccount = asyncRes.result().body()
                    response
                        .setStatusCode(201)
                        .setStatusMessage("Created")
                        .write(Json.encodePrettily(userAccount))
                        .end()
                } else {
                    response
                        .setStatusCode(400)
                        .setStatusMessage("Bad Request")
                        .write(asyncRes.toString())
                        .end()
                }
            }
        )
    }
}