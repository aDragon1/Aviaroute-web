package plugins

import io.ktor.server.application.*
import model.table.PersonTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
//    Database.connect(
//        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//        driver = "org.postgresql.Driver"
//    )
    val url = environment.config.property("postgres.url").getString()
    val driver = environment.config.property("postgres.driver").getString()
    val user = environment.config.property("postgres.user").getString()
    val pass = environment.config.property("postgres.password").getString()

    Database.connect(url = url, driver = driver, user = user, password = pass)

    transaction {
        SchemaUtils.create(PersonTable)
    }
}