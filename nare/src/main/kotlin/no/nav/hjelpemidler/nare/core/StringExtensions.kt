package no.nav.hjelpemidler.nare.core

internal fun String.singleQuoted(): String =
    if ((startsWith('\'') && endsWith('\'')) || (startsWith('(') && endsWith(')'))) {
        this
    } else {
        "'$this'"
    }
