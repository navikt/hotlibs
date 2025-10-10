package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.pgJsonbOf
import no.nav.hjelpemidler.database.toQueryParameters
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.år

data class TestEntity(
    val id: TestId = TestId(),
    @param:JsonAlias("string_1")
    val string: String = "",
    @param:JsonAlias("integer_1")
    val integer: Int = 0,
    @param:JsonAlias("long_1")
    val long: Int = 0,
    @param:JsonAlias("enum_1")
    val enum: TestEnum = TestEnum.A,
    @param:JsonAlias("data_1")
    val data1: Map<String, Any?> = emptyMap(),
    @param:JsonAlias("data_2")
    val data2: JsonNode? = null,
    @param:JsonAlias("fnr_1")
    val fnr: Fødselsnummer? = Fødselsnummer(50.år),
    @param:JsonAlias("aktor_id_1")
    val aktørId: AktørId? = AktørId("1234567891011"),
    val navn: Personnavn? = null,
) {
    fun toQueryParameters(): QueryParameters =
        mapOf(
            "string_1" to string,
            "integer_1" to integer,
            "long_1" to integer,
            "enum_1" to enum,
            "data_1" to pgJsonbOf(data1),
            "data_2" to pgJsonbOf(data2),
            "fnr_1" to fnr,
            "aktor_id_1" to aktørId,
        ) + (navn?.toQueryParameters() ?: emptyMap())
}

fun Row.toTestEntity(): TestEntity = TestEntity(
    id = testId(),
    string = string("string_1"),
    integer = int("integer_1"),
    enum = enum("enum_1"),
    data1 = json("data_1"),
    data2 = treeOrNull("data_2"),
    fnr = fødselsnummerOrNull("fnr_1"),
    aktørId = aktørIdOrNull("aktor_id_1"),
)
