package no.nav.hjelpemidler.behovsmeldingsmodell

import com.fasterxml.jackson.annotation.JsonAlias
import java.util.UUID

interface TilknyttetSøknad {
    @get:JsonAlias("soknadId")
    val søknadId: UUID
}

interface TilknyttetBehovsmelding {
    val behovsmeldingId: BehovsmeldingId
}
