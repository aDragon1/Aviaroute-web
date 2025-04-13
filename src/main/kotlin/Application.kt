package self.adragon

import io.ktor.server.application.*
import io.ktor.server.netty.*
import self.adragon.plugin.*


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureSockets()
    configureRouting()
    configureDatabaseRouting()
    configureHTTP()

    configureSerialization()

    configureDatabase()
//    Generator.generate()
}