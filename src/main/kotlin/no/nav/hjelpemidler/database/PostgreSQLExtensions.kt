package no.nav.hjelpemidler.database

import org.postgresql.util.PGobject

fun pgObjectOf(type: String, value: String): Any = PGobject().apply {
    this.type = type
    this.value = value
}

fun <T> pgJsonOf(value: T): Any =
    pgObjectOf(type = "json", value = jsonMapper.writeValueAsString(value))

fun <T> pgJsonbOf(value: T): Any =
    pgObjectOf(type = "jsonb", value = jsonMapper.writeValueAsString(value))
