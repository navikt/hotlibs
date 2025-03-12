package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.aktørIdOrNull
import no.nav.hjelpemidler.database.enum
import no.nav.hjelpemidler.database.fødselsnummerOrNull
import no.nav.hjelpemidler.database.json
import no.nav.hjelpemidler.database.pgJsonbOf
import no.nav.hjelpemidler.database.treeOrNull
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år

data class TestEntity(
    val id: TestId = TestId(),
    val string: String = "",
    val integer: Int = 0,
    val enum: TestEnum = TestEnum.A,
    val data1: Map<String, Any?> = emptyMap(),
    val data2: JsonNode? = null,
    val fnr: Fødselsnummer? = Fødselsnummer(50.år),
    val aktørId: AktørId? = AktørId("1234567891011"),
) {
    fun toQueryParameters(): QueryParameters =
        mapOf(
            "string" to string,
            "integer" to integer,
            "enum" to enum,
            "data_1" to pgJsonbOf(data1),
            "data_2" to pgJsonbOf(data2),
            "fnr" to fnr,
            "aktor_id" to aktørId,
        )
}

fun Row.toTestEntity(): TestEntity = TestEntity(
    id = testId(),
    string = string("string"),
    integer = int("integer"),
    enum = enum("enum"),
    data1 = json("data_1"),
    data2 = treeOrNull("data_2"),
    fnr = fødselsnummerOrNull("fnr"),
    aktørId = aktørIdOrNull("aktor_id"),
)
