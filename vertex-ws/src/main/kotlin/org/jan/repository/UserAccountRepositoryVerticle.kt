package org.jan.repository

import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple


const val ERROR_ON_SAVING_USER_ACCOUNT = 1

class UserAccountRepositoryVerticle : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().consumer(USER_ACCOUNT_REPOSITORY_INSERT,createUserAccount)
    }

    private val createUserAccount = Handler { message: Message<JsonObject> ->

        //save to postgres database
        // Connect options
        // Connect options
        val userAccount: JsonObject = message.body()

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
        val client: PgPool = PgPool.pool(vertx, connectOptions, poolOptions)

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
                    message.reply(userAccount)
                } else {
                    message.fail(
                        ERROR_ON_SAVING_USER_ACCOUNT,
                        "message: " + ar.cause().toString()
                    )
                }
                // Now close the pool
                client.close();
            }
        }
    }

    companion object {
        const val USER_ACCOUNT_REPOSITORY_INSERT: String = "USER_ACCOUNT_REPOSITORY_INSERT"
    }
}