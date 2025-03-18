package self.adragon.plugin

import database.table.PersonTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.*

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
        addLogger(StdOutSqlLogger)
//        SchemaUtils.dropCascade(PersonTable, Countries, Cities, Airports, DaysOfTheWeek)
//        SchemaUtils.dropCascade(DaysOfOperation, Airlines, DaysOfTheWeek, SeasonsOfTheYear, Segments, Routes)
//        SchemaUtils.dropCascade(AircraftTypes, Aircraft, Flights, FlightProblems, FlightSchedule)

        SchemaUtils.create(PersonTable, Countries, Cities, Airports, Airlines, DaysOfTheWeek, SeasonsOfTheYear)
        SchemaUtils.create(Segments, Routes, AircraftTypes, Aircraft, Flights, FlightProblems, FlightSchedule)
        SchemaUtils.create(DaysOfOperation)
    }
}

fun SchemaUtils.dropCascade(vararg tables: Table) = tables.forEach {
    TransactionManager.current().exec("DROP TABLE IF EXISTS ${it.tableName} CASCADE")
}
