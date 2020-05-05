package org.jan.controller

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple

object UserAccountController {

    val insertUser = Handler<RoutingContext> { routingContext ->

        val userAccount = routingContext.bodyAsJson

        //save to postgres database
        // Connect options
        // Connect options
        val response = routingContext.response()
            .putHeader("content-type", "application/json")
            .setChunked(true)

        val connectOptions = pgConnectOptionsOf(
            port = 5432,
            host = "172.22.0.2",
            database = "dev_db",
            user = "postgres",
            password = "postgres"
        )
        // Pool options

        // Pool options
        val poolOptions = poolOptionsOf()
            .setMaxSize(5)

        // Create the client pool
        val client: PgPool = PgPool.pool(routingContext.vertx(), connectOptions, poolOptions)

            // A simple query
        client.preparedQuery(
            "INSERT INTO user_account (name, password, email) VALUES ($1, $2, $3)",
            Tuple.of(
                userAccount.getString("name"),
                userAccount.getString("password"),
                userAccount.getString("email")
            )
        ) { ar ->
            run {
                if (ar.succeeded()) {
                    response
                        .setStatusCode(201)
                        .setStatusMessage("Created")
                        .write(Json.encodePrettily(userAccount))
                        .end()
                } else {
                    response
                        .setStatusCode(400)
                        .setStatusMessage("Bad Request")
                        .write(Json.encodePrettily("message" to ar.cause().toString()))
                        .end()
                }
                // Now close the pool
                client.close();
            }
        }
    }
}