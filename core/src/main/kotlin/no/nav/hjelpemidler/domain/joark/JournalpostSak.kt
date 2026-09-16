package no.nav.hjelpemidler.domain.joark

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.hjelpemidler.domain.joark.JournalpostSak.Fagsak
import no.nav.hjelpemidler.domain.joark.JournalpostSak.GenerellSak
import no.nav.hjelpemidler.domain.kodeverk.Fagsaksystem
import no.nav.hjelpemidler.domain.kodeverk.Fagsaktype
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "sakstype",
)
@JsonSubTypes(
    JsonSubTypes.Type(Fagsak::class, name = "FAGSAK"),
    JsonSubTypes.Type(GenerellSak::class, name = "GENERELL_SAK"),
)
sealed interface JournalpostSak {
    val sakstype: Fagsaktype

    data class Fagsak(
        val fagsakId: String,
        val fagsaksystem: Fagsaksystem,
    ) : JournalpostSak {
        override val sakstype = Fagsaktype.FAGSAK

        override fun toString(): String = "fagsakId: $fagsakId, fagsaksystem: $fagsaksystem"
    }

    data object GenerellSak : JournalpostSak {
        override val sakstype = Fagsaktype.GENERELL_SAK

        override fun toString(): String = "sak: GenerellSak"
    }
}

@OptIn(ExperimentalContracts::class)
val JournalpostSak.isFagsaksystemHotsak: Boolean
    get() {
        contract {
            returns(true) implies (this@isFagsaksystemHotsak is Fagsak)
        }
        return this is Fagsak && fagsaksystem == Fagsaksystem.HJELPEMIDLER
    }
