package no.nav.hjelpemidler.behovsmeldingsmodell.sak

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.hjelpemidler.behovsmeldingsmodell.BehovsmeldingStatus

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "system",
)
@JsonSubTypes(
    JsonSubTypes.Type(Sakstilknytning.Hotsak::class, name = Fagsak.System.HOTSAK_NAME),
    JsonSubTypes.Type(Sakstilknytning.Infotrygd::class, name = Fagsak.System.INFOTRYGD_NAME),
)
sealed interface Sakstilknytning {
    val sakId: Fagsak.Id
    val system: Fagsak.System
    val status: BehovsmeldingStatus?

    data class Hotsak(
        override val sakId: HotsakSakId,
        override val status: BehovsmeldingStatus? = null,
    ) : Sakstilknytning {
        override val system: Fagsak.System = Fagsak.System.HOTSAK
    }

    data class Infotrygd(
        override val sakId: InfotrygdSakId,
        val fnrBruker: String,
        override val status: BehovsmeldingStatus? = null,
    ) : Sakstilknytning {
        override val system: Fagsak.System = Fagsak.System.INFOTRYGD
    }
}
