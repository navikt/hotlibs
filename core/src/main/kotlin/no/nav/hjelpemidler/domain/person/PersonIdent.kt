package no.nav.hjelpemidler.domain.person

sealed interface PersonIdent : CharSequence {
    val value: String
}
