package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.QueryParametersProvider
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.toQueryParameters
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.år
import tools.jackson.databind.JsonNode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.util.UUID

data class TestEntity(
    val id: TestId = TestId(0),
    val boolean: Boolean? = null,
    val enum: TestEnum? = TestEnum.A,
    val integer: Int? = 10,
    val long: Long? = 20,
    val string: String? = "test",
    val uuid: UUID? = UUID.randomUUID(),
    val date: LocalDate? = LocalDate.now(),
    val time: LocalTime? = LocalTime.now().withNano(0),
    val timeWithTimezone: OffsetTime? = time?.atOffset(ZoneOffset.UTC),
    val timestamp: LocalDateTime? = LocalDateTime.of(date, time),
    val timestampWithTimezone: OffsetDateTime? = timestamp?.atOffset(ZoneOffset.UTC),
    val fnr: Fødselsnummer? = Fødselsnummer(50.år),
    val aktørId: AktørId? = AktørId("1234567891011"),
    val navn: Personnavn? = Personnavn("Grønn Rolig", null, "Bolle"),
    val strings: List<String>? = listOf("a", "b", "c"),
    val integers: List<Int>? = listOf(1, 2, 3),
    val data: JsonNode? = null,
) : QueryParametersProvider {
    override fun toQueryParameters(): QueryParameters {
        return mapOf(
            "boolean" to boolean,
            "enum" to enum,
            "integer" to integer,
            "long" to long,
            "string" to string,
            "uuid" to uuid,
            "date" to date,
            "time" to time,
            "time_with_timezone" to timeWithTimezone,
            "timestamp" to timestamp,
            "timestamp_with_timezone" to timestampWithTimezone,
            "fnr" to fnr,
            "aktor_id" to aktørId,
            "strings" to strings?.toTypedArray<String>(),
            "integers" to integers?.toTypedArray<Int>(),
            // "data" to pgJsonbOf(entity.data)
        ) + navn.toQueryParameters()
    }
}

fun Row.toTestEntity(): TestEntity = TestEntity(
    id = testId(),
    boolean = booleanOrNull("boolean"),
    enum = enumOrNull<TestEnum>("enum"),
    integer = intOrNull("integer"),
    long = longOrNull("long"),
    string = stringOrNull("string"),
    uuid = uuidOrNull("uuid"),
    date = localDateOrNull("date"),
    time = localTimeOrNull("time"),
    timeWithTimezone = offsetTimeOrNull("time_with_timezone"),
    timestamp = localDateTimeOrNull("timestamp"),
    timestampWithTimezone = offsetDateTimeOrNull("timestamp_with_timezone"),
    fnr = fødselsnummerOrNull("fnr"),
    aktørId = aktørIdOrNull("aktor_id"),
    navn = toPersonnavn(),
    strings = arrayOrNull<String>("strings")?.toList(),
    integers = arrayOrNull<Int>("integers")?.toList(),
    data = treeOrNull("data"),
)
