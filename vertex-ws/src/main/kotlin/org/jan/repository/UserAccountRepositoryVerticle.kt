package org.jan.repository

import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val ERROR_ON_SAVING_USER_ACCOUNT = 1

class UserAccountRepositoryVerticle : CoroutineVerticle() {

    private val conn: PgPool by lazy {
        val databaseConfig: JsonObject = config.getJsonObject("database")
        PgPool.pool(
            vertx,
            pgConnectOptionsOf(
                port = databaseConfig.getInteger("port"),
                host = databaseConfig.getString("host"),
                database = databaseConfig.getString("database"),
                user = databaseConfig.getString("user"),
                password = databaseConfig.getString("password")
            ),
            poolOptionsOf().setMaxSize(databaseConfig.getInteger("poolsize"))
        )
    }

    override suspend fun start() {
        vertx.eventBus().consumer(USER_ACCOUNT_REPOSITORY_INSERT,createUserAccount)
        println("repository verticle listening on $USER_ACCOUNT_REPOSITORY_INSERT")
    }

    private val createUserAccount = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            val userAccount: JsonObject = message.body()
            try {
                conn.preparedQuery("INSERT INTO user_account (name, password, email) VALUES ($1, $2, $3)")
                    .execute(
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