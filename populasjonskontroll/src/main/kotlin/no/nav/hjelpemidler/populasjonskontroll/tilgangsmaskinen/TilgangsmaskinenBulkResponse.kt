package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.http.HttpStatusCodeSerializer
import no.nav.hjelpemidler.serialization.jackson.personIdValue
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.deser.std.StdNodeBasedDeserializer

data class TilgangsmaskinenBulkResponse(
    val ansattId: NavIdent, // hvorfor heter ikke denne navIdent?
    val resultater: List<Resultat>,
) : Iterable<TilgangsmaskinenBulkResponse.Resultat> by resultater {
    @JsonDeserialize(using = Deserializer::class)
    sealed interface Resultat {
        val brukerId: PersonId

        @get:JsonSerialize(using = HttpStatusCodeSerializer::class)
        val status: HttpStatusCode

        data class Godkjent(
            override val brukerId: PersonId,
            override val status: HttpStatusCode = HttpStatusCode.NoContent,
        ) : Resultat

        data class Avvist(
            override val brukerId: PersonId,
            override val status: HttpStatusCode = HttpStatusCode.Forbidden,
            val detaljer: TilgangsmaskinenResponse.Avvist = TilgangsmaskinenResponse.AVVIST,
        ) : Resultat
    }

    fun associateByBrukerId(): Map<PersonId, Resultat> = associateBy(Resultat::brukerId)

    private class Deserializer : StdNodeBasedDeserializer<Resultat>(Resultat::class.java) {
        override fun convert(root: JsonNode, context: DeserializationContext): Resultat {
            val brukerId = root["brukerId"].personIdValue()
            val status = HttpStatusCode.fromValue(root["status"].intValue())
            return if (status.isSuccess()) {
                Resultat.Godkjent(
                    brukerId = brukerId,
                    status = status,
                )
            } else {
                Resultat.Avvist(
                    brukerId = brukerId,
                    status = status,
                    detaljer = context.readTreeAsValue(root["detaljer"], TilgangsmaskinenResponse.Avvist::class.java),
                )
            }
        }
    }

    companion object {
        fun godkjent(personIder: Set<PersonId>): TilgangsmaskinenBulkResponse = TilgangsmaskinenBulkResponse(
            ansattId = NavIdent.UKJENT,
            resultater = personIder.map(Resultat::Godkjent)
        )

        fun avvist(personIder: Set<PersonId>): TilgangsmaskinenBulkResponse = TilgangsmaskinenBulkResponse(
            ansattId = NavIdent.UKJENT,
            resultater = personIder.map(Resultat::Avvist)
        )
    }
}
