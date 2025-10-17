package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.QueryParametersProvider
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.annotation.Table
import no.nav.hjelpemidler.database.toQueryParameters
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.år
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.util.UUID

@Table("test")
data class TestEntity(
    val id: TestId = TestId(0),
    val integer: Int = 10,
    val long: Long = 20,
    val string: String = "test",
    val enum: TestEnum = TestEnum.A,
    val dataRequired: Map<String, Any?> = emptyMap(),
    val dataOptional: JsonNode? = null,
    val fnr: Fødselsnummer? = Fødselsnummer(50.år),
    @param:JsonAlias("aktor_id")
    val aktørId: AktørId? = AktørId("1234567891011"),
    val navn: Personnavn? = Personnavn("Fornavn", null, "Etternavn"),
    val booleanRequired: Boolean = false,
    val booleanOptional: Boolean? = null,
    val date: LocalDate? = LocalDate.now(),
    val time: LocalTime? = LocalTime.now().withNano(0),
    val timeWithTimezone: OffsetTime? = time?.atOffset(ZoneOffset.UTC),
    val timestamp: LocalDateTime? = LocalDateTime.of(date, time),
    val timestampWithTimezone: OffsetDateTime? = timestamp?.atOffset(ZoneOffset.UTC),
    val arrayString: List<String>? = listOf("a", "b", "c"),
    val arrayInteger: List<Int>? = listOf(1, 2, 3),
    val uuid: UUID? = UUID.randomUUID(),
) : QueryParametersProvider {
    override fun toQueryParameters(): QueryParameters {
        return mapOf(
            "integer" to integer,
            "long" to long,
            "string" to string,
            "enum" to enum,
            "fnr" to fnr,
            "aktor_id" to aktørId,
            "boolean_required" to booleanRequired,
            "boolean_optional" to booleanOptional,
            "date" to date,
            "time" to time,
            "time_with_timezone" to timeWithTimezone,
            "timestamp" to timestamp,
            "timestamp_with_timezone" to timestampWithTimezone,
            "array_string" to arrayString?.toTypedArray<String>(),
            "array_integer" to arrayInteger?.toTypedArray<Int>(),
            "uuid" to uuid,
        ) + (navn?.toQueryParameters() ?: emptyMap())
    }
}

fun Row.toTestEntity(): TestEntity = TestEntity(
    id = testId(),
    integer = int("integer"),
    long = long("long"),
    string = string("string"),
    enum = enum("enum"),
    dataRequired = json("data_required"),
    dataOptional = treeOrNull("data_optional"),
    fnr = fødselsnummerOrNull("fnr"),
    aktørId = aktørIdOrNull("aktor_id"),
    navn = stringOrNull("navn")?.let {
        val (fornavn, mellomnavn, etternavn) = it.removeSurrounding("(", ")").split(",")
        Personnavn(fornavn, mellomnavn.takeIf(String::isNotEmpty), etternavn)
    },
    booleanRequired = boolean("boolean_required"),
    booleanOptional = booleanOrNull("boolean_optional"),
    date = localDateOrNull("date"),
    time = localTimeOrNull("time"),
    timeWithTimezone = offsetTimeOrNull("time_with_timezone"),
    timestamp = localDateTimeOrNull("timestamp"),
    timestampWithTimezone = offsetDateTimeOrNull("timestamp_with_timezone"),
    arrayString = arrayOrNull<String>("array_string")?.toList(),
    arrayInteger = arrayOrNull<Int>("array_integer")?.toList(),
    uuid = uuidOrNull("uuid"),
)
