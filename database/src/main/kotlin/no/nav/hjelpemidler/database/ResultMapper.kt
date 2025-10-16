package no.nav.hjelpemidler.database

fun interface ResultMapper<T : Any> : (Row) -> T?
