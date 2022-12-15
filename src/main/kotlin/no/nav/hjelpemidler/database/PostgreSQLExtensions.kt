package no.nav.hjelpemidler.database

import org.postgresql.util.PGobject

fun pgObjectOf(type: String, value: String): PGobject = PGobject().apply {
    this.type = type
    this.value = value
}

fun <T> jsonOf(value: T): PGobject =
    pgObjectOf(type = "json", value = jsonMapper.writeValueAsString(value))

fun <T> jsonbOf(value: T): PGobject =
    pgObjectOf(type = "jsonb", value = jsonMapper.writeValueAsString(value))
