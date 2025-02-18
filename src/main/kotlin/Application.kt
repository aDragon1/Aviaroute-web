package self.adragon

import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureRouting()
    configureHTTP()

    configureDatabases()
    configureSerialization()
}