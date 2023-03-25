package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.pgJsonbOf
import java.time.Instant

data class Test1Entity(
    val id: Long = -1,
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
            "enum" to enum.name,
            "data_1" to pgJsonbOf(data1),
            "data_2" to pgJsonbOf(data2),
        )
}

data class Test2Entity(
    val id: Long = -1,
    val boolean: Boolean,
    val instant: Instant = Instant.MIN,
) {
    fun toQueryParameters(): QueryParameters =
        mapOf(
            "boolean" to boolean,
            "instant" to instant,
        )
}
