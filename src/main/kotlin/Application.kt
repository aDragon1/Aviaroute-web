package self.adragon

import io.ktor.server.application.*
import plugins.configureHTTP
import plugins.configureRouting
import plugins.configureSerialization
import plugins.configureSockets
import io.ktor.server.netty.EngineMain
import plugins.configureDatabase


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureRouting()
    configureHTTP()

    configureSerialization()
    configureDatabase()
}