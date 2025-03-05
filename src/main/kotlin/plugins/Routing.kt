package plugins

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
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.ul
import model.routing.configurePersonRouting
import javax.net.ssl.SSLHandshakeException

fun Application.configureRouting() {

    configurePersonRouting()

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ -> call.respondText(" Url route not found.") }

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        exception<SSLHandshakeException> { call, cause ->
            call.respondText { "$cause" }
            println("Exception occurred because of $cause")
        }
    }

    routing {
        get { call.respondRedirect("/hello") }
        staticResources("/static", "static")

        get("/text") { call.respondText("Hello, World!") }

        get("/hello") {
            call.respondHtml {
                body {
                    h1 { +"Hello from ktor!" }
                    ul { +"This is CLI-app only, for now. Please, use Postman or whatever to interact w/ it" }
                }
            }
        }

        webSocket("/echo") {
            while (isActive) {
                val receivedRaw = incoming.receive().readBytes()
                val received = String(receivedRaw)
                println("Received (raw) - $receivedRaw")

                if (received.trim().lowercase() == "close") {
                    close(CloseReason(CloseReason.Codes.NORMAL, "User pleased to close"))
                    return@webSocket
                }

                send(Frame.Text("Server: $received"))
            }
        }
    }
}