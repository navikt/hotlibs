package no.nav.hjelpemidler.database

fun interface ResultMapper<out T : Any> : (Row) -> T?
