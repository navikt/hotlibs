package no.nav.hjelpemidler.database.sql

import no.nav.hjelpemidler.database.QueryParameter

interface SqlEnum<E : Enum<E>> : QueryParameter<String> {
    val name: String

    override val queryParameter: String get() = name
}
