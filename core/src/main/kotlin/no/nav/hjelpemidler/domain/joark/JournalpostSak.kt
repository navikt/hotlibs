package no.nav.hjelpemidler.domain.joark

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.hjelpemidler.domain.kodeverk.Fagsaksystem
import no.nav.hjelpemidler.domain.kodeverk.Fagsaktype

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "sakstype",
)
@JsonSubTypes(
    JsonSubTypes.Type(JournalpostSak.Fagsak::class, name = "FAGSAK"),
    JsonSubTypes.Type(JournalpostSak.GenerellSak::class, name = "GENERELL_SAK"),
)
sealed interface JournalpostSak {
    val sakstype: Fagsaktype

    data class Fagsak(
        val fagsakId: String,
        val fagsaksystem: Fagsaksystem,
    ) : JournalpostSak {
        override val sakstype = Fagsaktype.FAGSAK

        val isFagsaksystemHotsak: Boolean @JsonIgnore get() = fagsaksystem == Fagsaksystem.HJELPEMIDLER

        override fun toString(): String = "fagsakId: $fagsakId, fagsaksystem: $fagsaksystem"
    }

    data object GenerellSak : JournalpostSak {
        override val sakstype = Fagsaktype.GENERELL_SAK
    }
}
