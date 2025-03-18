package self.adragon

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.launch
import self.adragon.plugin.configureDatabase
import self.adragon.plugin.configureDatabaseRouting
import self.adragon.plugin.configureHTTP
import self.adragon.plugin.configureRouting
import self.adragon.plugin.configureSerialization
import self.adragon.plugin.configureSockets


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureRouting()
    configureDatabaseRouting()
    configureHTTP()

    configureSerialization()
    configureDatabase()

//    launch { Generator.generate() }
}