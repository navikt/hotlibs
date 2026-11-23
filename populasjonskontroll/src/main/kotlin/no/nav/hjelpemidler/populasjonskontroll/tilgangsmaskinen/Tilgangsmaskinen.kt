package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.security.AutentisertAnsatt

interface Tilgangsmaskinen {
    suspend fun evaluerKjernereglerForAnsatt(
        ansatt: AutentisertAnsatt,
        personId: PersonId,
    ): TilgangsmaskinenResponse

    suspend fun evaluerKjernereglerForAnsatt(
        ansatt: AutentisertAnsatt,
        personIder: Set<PersonId>,
    ): TilgangsmaskinenBulkResponse

    enum class RegelType { KJERNE_REGELTYPE, KOMPLETT_REGELTYPE, OVERSTYRBAR_REGELTYPE }

    object KunApplikasjon : Tilgangsmaskinen {
        override suspend fun evaluerKjernereglerForAnsatt(
            ansatt: AutentisertAnsatt,
            personId: PersonId,
        ): TilgangsmaskinenResponse = TilgangsmaskinenResponse.AVVIST

        override suspend fun evaluerKjernereglerForAnsatt(
            ansatt: AutentisertAnsatt,
            personIder: Set<PersonId>,
        ): TilgangsmaskinenBulkResponse = TilgangsmaskinenBulkResponse.avvist(personIder)
    }
}
