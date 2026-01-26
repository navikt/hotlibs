package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.hjelpemidler.domain.id.StringId

@JsonDeserialize(using = UtførtAvIdDeserializer::class)
sealed class UtførtAvId(value: String) : StringId(value)

fun utførtAvIdOf(value: String): UtførtAvId = if (NavIdent.erGyldig(value)) NavIdent(value) else Systemnavn(value)
