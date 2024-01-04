package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.pgJsonbOf

data class TestEntity(
    val id: TestId = TestId(),
    val string: String,
    val integer: Int,
    val enum: TestEnum,
    val data1: Map<String, Any?>,
    val data2: Map<String, Any?>? = null,
) {
    fun toQueryParameters(): QueryParameters =
        mapOf(
            "string" to string,
            "integer" to integer,
            "enum" to enum,
            "data_1" to pgJsonbOf(data1),
            "data_2" to pgJsonbOf(data2),
        )
}
