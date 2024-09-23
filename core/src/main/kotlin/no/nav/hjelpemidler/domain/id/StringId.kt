package no.nav.hjelpemidler.domain.id

abstract class StringId(value: String) : Id<String>(value), CharSequence by value
