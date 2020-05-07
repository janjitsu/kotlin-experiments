package org.jan

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jan.controller.UserAccountController


class ServerVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        router.post("/user").handler(UserAccountController.insertUser)

        val httpServer = vertx.createHttpServer()
        httpServer
            .requestHandler(router)
            .listenAwait(config.getJsonObject("server").getInteger("port"))

        println("Server started in port ${httpServer.actualPort()}")
    }

}
