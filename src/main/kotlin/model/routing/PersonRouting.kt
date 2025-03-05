package model.routing

import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import kotlinx.coroutines.isActive
import kotlinx.html.body
import kotlinx.html.ul
import model.services.PersonRepository

fun Application.configurePersonRouting() {
    routing {
        get("dbtest/{name}") {
            val name = call.parameters["name"] ?: "error"
            val age = (1..20).random()

            val new = PersonRepository.insert(name, age).resultedValues?.first()
            call.respondText { "Inserted - $new" }
        }

        get("dbtest") {
            val persons = PersonRepository.selectAll()
            println("Persons in db - ${persons.size}")
            call.respondHtml {
                body {
                    ul { +"Here's list of all persons:" }
                    persons.forEach { ul { +"$it" } }
                }
            }
        }

        webSocket("dbtest/echo") {
            val numsRegEx = "-?[0-9]+(\\.[0-9]+)?".toRegex()

            while (isActive) {
                val receivedRaw = incoming.receive().readBytes()
                val received = String(receivedRaw)

                if (!received.matches(numsRegEx)) {
                    send(Frame.Text("$received is not number. Please, enter valid ID"))
                    continue
                }

                val receivedPerson = PersonRepository.selectById(received.toInt())
                if (receivedPerson == null) {
                    send(Frame.Text("No person with id $received"))
                    continue
                }

                sendSerialized(receivedPerson)
            }
        }
    }
}