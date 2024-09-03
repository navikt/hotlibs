package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.pgJsonbOf
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år

data class TestEntity(
    val id: TestId = TestId(),
    val string: String,
    val integer: Int,
    val enum: TestEnum,
    val data1: Map<String, Any?>,
    val data2: Map<String, Any?>? = null,
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
