package no.nav.hjelpemidler.validering

import no.nav.hjelpemidler.text.isInteger

fun nummerValidator(lengde: Int): Validator<String> =
    Validator { value ->
        value.length == lengde && value.isInteger()
    }
