package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.StringId
import tools.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = UtførtAvIdDeserializer::class)
sealed class UtførtAvId(value: String) : StringId(value)

fun utførtAvIdOf(value: String): UtførtAvId = if (NavIdent.erGyldig(value)) NavIdent(value) else Systemnavn(value)
