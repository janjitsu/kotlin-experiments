package org.jan.repository

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.pgclient.preparedQueryAwait
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val ERROR_ON_SAVING_USER_ACCOUNT = 1

class UserAccountRepositoryVerticle : CoroutineVerticle() {

    private val connectOptions = pgConnectOptionsOf(
            port = 5432,
            host = "172.19.0.2",
            database = "dev_db",
            user = "postgres",
            password = "postgres"
    )
    // Pool options

    // Pool options
    private val poolOptions = poolOptionsOf()
            .setMaxSize(5)

    private fun getConn(vertx: Vertx) : PgPool = PgPool.pool(vertx, connectOptions, poolOptions)

    override suspend fun start() {
        vertx.eventBus().consumer(USER_ACCOUNT_REPOSITORY_INSERT,createUserAccount)
    }

    private val createUserAccount = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            val userAccount: JsonObject = message.body()

            try {
                getConn(vertx).preparedQueryAwait(
                        "INSERT INTO user_account (name, password, email) VALUES ($1, $2, $3)",
                        Tuple.of(
                                userAccount.getString("name"),
                                userAccount.getString("password"),
                                userAccount.getString("email")
                        )
                )
                message.reply(userAccount)
            } catch (e: Exception) {
                message.fail(ERROR_ON_SAVING_USER_ACCOUNT, "message: " + e.message)
            }
        }
    }

    companion object {
        const val USER_ACCOUNT_REPOSITORY_INSERT: String = "USER_ACCOUNT_REPOSITORY_INSERT"
    }
}