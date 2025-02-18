package self.adragon

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.html.*

fun Application.configureRouting() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, code ->
            call.respondText(" Url route not found.")
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        exception<javax.net.ssl.SSLHandshakeException> { call, cause ->
            call.respondText { "$cause" }
            println("Exception occurred because of $cause")
        }
    }
    routing {
        staticResources("/static", "static")

        get("/") {
            call.respondHtml {
                body {
                    h1 { +"Hello from ktor!" }
                    ul { +"This is CLI-app only, for now. Please, use Postman or whatever to interact w/ it" }
                }
            }
        }
        get("/hello") {
            call.respondText("Hello, World!")
        }

        webSocket("/echo") {
            while (isActive) {
                val received = String(incoming.receive().readBytes())

                if (received.trim().lowercase() == "close") {
                    close(CloseReason(CloseReason.Codes.NORMAL, "User pleased to close"))
                    return@webSocket
                }

                println("Received - $received")
                send(Frame.Text("Server: $received"))
            }
        }
    }
}
