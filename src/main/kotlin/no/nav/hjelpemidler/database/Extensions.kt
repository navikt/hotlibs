package no.nav.hjelpemidler.database

fun Int?.toQueryParameters(key: String = "id"): QueryParameters =
    mapOf(key to this)

fun Long?.toQueryParameters(key: String = "id"): QueryParameters =
    mapOf(key to this)

fun String?.toQueryParameters(key: String = "id"): QueryParameters =
    mapOf(key to this)
