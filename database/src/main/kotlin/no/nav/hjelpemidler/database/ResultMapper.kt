package no.nav.hjelpemidler.database

fun interface ResultMapper<T> : (Row) -> T?
