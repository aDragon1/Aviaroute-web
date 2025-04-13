package self.adragon.routing

import database.table.PersonTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import self.adragon.database.repository.PersonRepository
import self.adragon.model.db.Person

private const val MAIN_ROUTE = "/persons"
fun Application.configurePersonRouting() {
    routing {
        post("$MAIN_ROUTE/insert") {
            val params = call.receiveParameters()

            val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Имя не указано")
            val tmp = params["age"]?.toIntOrNull()

            val age = if (!(tmp == null || tmp < 0)) tmp else return@post call.respond(
                HttpStatusCode.BadRequest,
                "Некорректный возраст"
            )

            try {
                PersonRepository.insert(name, age)
                val persons = PersonRepository.selectAll()

                val personsList = persons.map { Person.fromRow(it) }

                val personsJson = Json.encodeToString(personsList)
                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "status" to "OK",
                        "message" to "Добавление прошло успешно",
                        "persons" to personsJson
                    )
                )

            } catch (e: ExposedSQLException) {
                println(e.causedByQueries())
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("status" to "error", "message" to "Ошибка при добавлении")
                )
            } catch (e: Exception) {
                println(e.message)
            }
        }

        delete(MAIN_ROUTE) {
            val params = call.receiveParameters()

            val id = params["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Айди для удаления не указан"
            )

            try {
                PersonRepository.delete(id)
                val persons = PersonRepository.selectAll()


                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "status" to "OK",
                        "message" to "Удаление прошло успешно",
                        "personsSize" to "${persons.size}"
                    )
                )

            } catch (e: ExposedSQLException) {
                println(e.causedByQueries())
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("status" to "error", "message" to "Ошибка при удалении")
                )
            } catch (e: Exception) {
                println(e.message)
            }
        }

        get(MAIN_ROUTE) {
            val persons = PersonRepository.selectAll()

            call.respondHtml(HttpStatusCode.OK) {
                head { link(rel = "stylesheet", href = "/static/styles.css") }

                body {
                    createPersonsForm()

                    ul {
                        id = "tableEntriesCount"
                        +"Количество записей в таблице - ${persons.size}"
                    }
                    createPersonsTable(persons)
                }
            }
        }
    }
}

fun SCRIPT.personsValidateScript() = unsafe {
    raw(
        """
async function validateForm(event) {
    event.preventDefault();

    let nameInput = document.getElementById("nameInput");
    let ageInput = document.getElementById("ageInput");
    let nameError = document.getElementById("nameError");
    let ageError = document.getElementById("ageError");

    nameError.textContent = "";
    ageError.textContent = "";

    let valid = true;

    if (nameInput.value.trim() === "") {
        nameError.textContent = "Поле \"Имя\" не может быть пустым (:";
        nameInput.style.borderColor = "red";
        valid = false;
    } else nameInput.style.borderColor = "";

    let ageValue = parseInt(ageInput.value);
    if (isNaN(ageValue) || ageValue < 0) {
        ageError.textContent = "Поле \"Возраст\" не может быть пустым (:";
        ageInput.style.borderColor = "red";
        valid = false;
    } else ageInput.style.borderColor = "";

    if (!valid) return;

    const data = new FormData();
    data.append("name", nameInput.value);
    data.append("age", ageInput.value);

    const response = await fetch("$MAIN_ROUTE/insert", {
        method: "POST",
        body: data
    });

    const result = await response.json();
    
    nameInput.value = "";
    ageInput.value = "";
    
    if (result.status === "OK") updatePersonsTable(JSON.parse(result.persons));
}
""".trimIndent()
    )
}

fun SCRIPT.personsDeleteRow() = unsafe {
    raw(
        """
async function deleteRow(button) {
    const row = button.closest("tr");
    if (!row) return

    const idCell = row.querySelector("td");
    const idValue = idCell ? idCell.textContent.trim() : "неизвестно";

    const data = new FormData();
    data.append("id", idValue);

    const response = await fetch("$MAIN_ROUTE", {
        method: "DELETE",
        body: data
    });
    const result = await response.json();
    
    document.getElementById("tableEntriesCount").textContent = "Количество записей в таблице - " + result.personsSize;
    row.remove()
}
    """.trimIndent()
    )
}

fun SCRIPT.personsUpdateTable() = unsafe {
    raw(
        """
function updatePersonsTable(persons) {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = ""; // Очищаем текущую таблицу

    persons.forEach(person => {
        const tr = document.createElement("tr");

        const tdId = document.createElement("td");
        tdId.textContent = person.id;
        tr.appendChild(tdId);

        const tdName = document.createElement("td");
        tdName.textContent = person.name;
        tr.appendChild(tdName);

        const tdAge = document.createElement("td");
        tdAge.textContent = person.age;
        tr.appendChild(tdAge);

        const tdDelete = document.createElement("td");
        tdDelete.style.verticalAlign = "middle";
        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.classList.add("delete-button");
        deleteButton.textContent = "−";
        deleteButton.addEventListener("click", function() {
            deleteRow(this);
        });
        tdDelete.appendChild(deleteButton);
        tr.appendChild(tdDelete);

        tbody.appendChild(tr);
    });
    document.getElementById("tableEntriesCount").textContent = "Количество записей в таблице - " + persons.length;
}
    """.trimIndent()
    )
}


fun FlowContent.createPersonsForm() {
    form {
        id = "personForm"
        action = "$MAIN_ROUTE/insert"
        method = FormMethod.post

        textInput {
            id = "nameInput"
            name = "name"
            placeholder = "Имя"
        }
        span("error-message") { id = "nameError" }

        numberInput {
            id = "ageInput"
            name = "age"
            placeholder = "Возраст"
        }
        span("error-message") { id = "ageError" }

        button {
            type = ButtonType.button
            onClick = "validateForm(event)"
            +"Добавить"
        }
    }

    script {
        personsValidateScript()
        personsUpdateTable()
        personsDeleteRow()
    }
}

fun FlowContent.createPersonsTable(persons: List<ResultRow>) {
    table {
        thead {
            tr {
                th { +"ID" }
                th { +"Имя" }
                th { +"Возраст" }
                th { }
            }
        }
        tbody {
            persons.forEach {
                val id = it[PersonTable.id]
                val name = it[PersonTable.name]
                val age = it[PersonTable.age]

                tr {
                    td { +"$id" }
                    td { +name }
                    td { +"$age" }
                    td {
                        attributes["style"] = "vertical-align: middle;"
                        button(classes = "delete-button") {
                            attributes["type"] = "button"
                            onClick = "deleteRow(this);"
                            +"−"
                        }
                    }
                }
            }
        }
    }
}