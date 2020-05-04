package org.jan

import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple


class ServerVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val port = 8080
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        router.get("/").handler(getHi)
        router.get("/test/:name").handler(testName)
        router.post("/user").handler(processUser)

        val httpServer = vertx.createHttpServer()
        httpServer
            .requestHandler(router)
            .listen(port)

        println("Server started in port $port")
    }

    private val getHi = Handler<RoutingContext> { routingContext ->
        routingContext.response()
            .putHeader("content-type", "text/plain")
            .setChunked(true)
            .write("Hi")
            .end()
    }

    private val testName = Handler<RoutingContext>  { routingContext ->
        val request = routingContext.request()
        val name = request.getParam("name")
        val response = routingContext.response()

        response.putHeader("content-type", "application/json")
            .setChunked(true)
            .write(Json.encodePrettily(ResponseObj(name)))
            .end()
    }

    private val processUser = Handler<RoutingContext> { routingContext ->

        val body = routingContext.bodyAsJson
        val name = body.getString("name")

        //save to postgres database
        // Connect options
        // Connect options

        val connectOptions = pgConnectOptionsOf(
            port = 5432,
            host = "172.21.0.2",
            database = "dev_db",
            user = "postgres",
            password = "postgres"
        )
        // Pool options

        // Pool options
        val poolOptions = poolOptionsOf()
            .setMaxSize(5)

        // Create the client pool

        // Create the client pool
        val client: PgPool = PgPool.pool(vertx, connectOptions, poolOptions)

        // A simple query
        client.preparedQuery(
            "INSERT INTO user_account (name, password, email) VALUES (\$$1, \$$2, \$$3)",
            Tuple.of("jan", "123456", "janfrs3@gmail.com")
        ) { ar ->
            run {
                if (ar.succeeded()) {
                    val result = ar.result();
                    println("Got " + result.size() + " rows ");
                } else {
                    println("Failure: " + ar.cause().toString());
                }
                // Now close the pool
                client.close();
            }
        }

        val response = routingContext.response()

        response.putHeader("content-type", "application/json")
            .setChunked(true)
            .setStatusCode(201)
            .setStatusMessage("Created")
            .write(Json.encodePrettily(ResponseObj(name)))
            .end()
    }
}

/* Data classes provide a better approach than Pair or Triple */
data class ResponseObj(val name: String = "")