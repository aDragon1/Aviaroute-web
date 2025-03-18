package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Segments

object SegmentRepository {
    fun insert(pRouteId: Int, pFlightId: Int, pPosition: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Segments.insert {
            it[routeId] = pRouteId
            it[flightId] = pFlightId
            it[position] = pPosition
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Segments.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Segments.selectAll().where { Segments.id eq id }.map { it }
    }.singleOrNull()
}