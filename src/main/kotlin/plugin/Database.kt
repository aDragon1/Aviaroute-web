package self.adragon.plugin

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
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

        // Done
//        SchemaUtils.dropCascade(DaysOfTheWeekTable, SeasonsOfTheYearTable, CountriesTable, CitiesTable, AirportsTable)
//        SchemaUtils.dropCascade(AirlinesTable, AircraftTypesTable, AircraftTable, FlightStatusesTable)
//        SchemaUtils.dropCascade(FlightsTable, FlightScheduleTable, DaysOfOperationTable)
//        SchemaUtils.dropCascade(SegmentsTable, RoutesTable, FlightProblemsTable)

        SchemaUtils.create(CountriesTable, CitiesTable, AirportsTable, AirlinesTable, DaysOfTheWeekTable)
        SchemaUtils.create(SeasonsOfTheYearTable, DaysOfOperationTable, FlightProblemsTable, FlightScheduleTable)
        SchemaUtils.create(SegmentsTable, RoutesTable, AircraftTypesTable, AircraftTable, FlightsTable)
        SchemaUtils.create(FlightStatusesTable)
        createFlightsTrigger()
    }
}

fun SchemaUtils.dropCascade(vararg tables: Table) = tables.forEach {
    TransactionManager.current().exec("DROP TABLE IF EXISTS ${it.tableName} CASCADE")
}

fun createFlightsTrigger() {
    TransactionManager.current().exec(
        "CREATE OR REPLACE FUNCTION public.check_flights_consistency()\n" +
                "    RETURNS trigger\n" +
                "    LANGUAGE 'plpgsql'\n" +
                "    VOLATILE\n" +
                "    COST 100\n" +
                "AS $$\n" +
                "DECLARE\n" +
                "    schedule_airline_id INTEGER;\n" +
                "    aircraft_airline_id INTEGER;\n" +
                "\teconomy INTEGER;\n" +
                "\tpriority INTEGER;\n" +
                "\tbusiness INTEGER;\n" +
                "BEGIN \n" +
                "    SELECT airline_id INTO schedule_airline_id \n" +
                "    FROM flight_schedule \n" +
                "    WHERE id = NEW.flight_number;\n" +
                "\n" +
                "    SELECT airline_id INTO aircraft_airline_id \n" +
                "    FROM aircraft \n" +
                "    WHERE id = NEW.aircraft_id;\n" +
                "\n" +
                "\tSELECT economy_total, priority_total, business_total INTO economy, priority, business \n" +
                "\tFROM aircraft_types\n" +
                "\tWHERE id = NEW.aircraft_id;\n" +
                "\n" +
                "\tIF NEW.economy_left < 0 OR NEW.economy_left > economy THEN\n" +
                "\t\tRAISE EXCEPTION 'Количество мест в эконом-классе для данного типа воздушного судна не может быть отрицательным или больше % (текущее значение: %).', economy, NEW.economy_left;\n" +
                "    END IF;\n" +
                "\tIF NEW.priority_left < 0 OR NEW.priority_left > priority THEN\n" +
                "\t\tRAISE EXCEPTION 'Количество мест в премиум-классе для данного типа воздушного судна не может быть отрицательным или больше % (текущее значение: %).', priority, NEW.priority;\n" +
                "    END IF;\n" +
                "\tIF NEW.business_left < 0 OR NEW.business_left > business THEN\n" +
                "\t\tRAISE EXCEPTION 'Количество мест в бизнесс-классе для данного типа воздушного судна не может быть отрицательным или больше % (текущее значение: %).', economy, NEW.business_left;\n" +
                "    END IF;\n" +
                "\t\n" +
                "    IF schedule_airline_id IS NULL THEN\n" +
                "        RAISE EXCEPTION 'Расписание рейса (flight_number = %) не найдено', NEW.flight_number;\n" +
                "    ELSIF aircraft_airline_id IS NULL THEN\n" +
                "        RAISE EXCEPTION 'Самолет (aircraft_id = %) не найден', NEW.aircraft_id;\n" +
                "    ELSIF schedule_airline_id <> aircraft_airline_id THEN\n" +
                "        RAISE EXCEPTION 'Несоответствие авиакомпаний: расписание (airline_id = %) не совпадает с самолетом (airline_id = %)', schedule_airline_id, aircraft_airline_id;\n" +
                "    END IF;\n" +
                "\n" +
                "    RETURN NEW; \n" +
                "END;\n" +
                "$$;\n" +
                "\n" +
                "CREATE OR REPLACE TRIGGER enforce_flights_consistency\n" +
                "BEFORE INSERT OR UPDATE ON flights\n" +
                "FOR EACH ROW\n" +
                "EXECUTE FUNCTION public.check_flights_consistency();"
    )
}