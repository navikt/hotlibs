package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.kafka.Event
import no.nav.hjelpemidler.kafka.EventName
import java.util.UUID

@EventName("hm-test-event")
data class TestEvent(
    val id: String,
    val vedtakId: String?,
    @JsonAlias("soknadId")
    val søknadId: UUID,
    @JsonProperty("fnrBruker")
    val brukerFnr: String,
    override val eventId: UUID = UUID.randomUUID(),
) : Event
