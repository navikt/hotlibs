package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonValue

sealed interface PersonIdent : CharSequence {
    @get:JsonValue
    val value: String
}
