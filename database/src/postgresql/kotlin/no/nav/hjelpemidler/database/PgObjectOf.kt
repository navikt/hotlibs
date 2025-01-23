package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.writeValueAsStringOrNull
import org.postgresql.util.PGobject

fun pgObjectOf(type: String, value: String?): Any =
    PGobject().apply {
        this.type = type
        this.value = value
    }

fun pgJsonOf(value: Any?): Any =
    pgObjectOf(type = "json", value = jsonMapper.writeValueAsStringOrNull(value))

fun pgJsonbOf(value: Any?): Any =
    pgObjectOf(type = "jsonb", value = jsonMapper.writeValueAsStringOrNull(value))
