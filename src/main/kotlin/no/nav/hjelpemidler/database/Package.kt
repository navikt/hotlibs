package no.nav.hjelpemidler.database

internal fun prefix(value: String): String =
    "${object {}.javaClass.packageName}.$value".replace(".", "_")
