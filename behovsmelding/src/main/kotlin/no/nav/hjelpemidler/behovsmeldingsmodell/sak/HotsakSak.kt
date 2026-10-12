package no.nav.hjelpemidler.behovsmeldingsmodell.sak

import java.time.Instant
import java.util.UUID

data class HotsakSak(
    override val sakId: HotsakSakId,
    override val søknadId: UUID,
    override val opprettet: Instant,
    override val vedtak: Vedtak? = null,
) : Fagsak {
    override val system: Fagsak.System = Fagsak.System.HOTSAK
}
