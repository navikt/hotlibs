package no.nav.hjelpemidler.behovsmeldingsmodell

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.hjelpemidler.behovsmeldingsmodell.sak.Sakstilknytning
import no.nav.hjelpemidler.domain.kodeverk.Kanal
import java.util.UUID

/**
 * Grunnlag for lagring av behovsmelding.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "kilde",
)
@JsonSubTypes(
    JsonSubTypes.Type(Behovsmeldingsgrunnlag.Digital::class, name = "DIGITAL"),
    JsonSubTypes.Type(Behovsmeldingsgrunnlag.Papir::class, name = "PAPIR"),
    JsonSubTypes.Type(Behovsmeldingsgrunnlag.Journalpost::class, name = "JOURNALPOST"),
)
sealed interface Behovsmeldingsgrunnlag : TilknyttetSøknad {
    val status: BehovsmeldingStatus
    val fnrBruker: String
    val navnBruker: String
    val kilde: Kilde

    @get:JsonAlias("er_digital")
    val digital: Boolean

    /**
     * Digital behovsmelding.
     */
    data class Digital(
        override val søknadId: BehovsmeldingId,
        override val status: BehovsmeldingStatus,
        override val fnrBruker: String,
        override val navnBruker: String,
        val fnrInnsender: String?,
        @JsonAlias("soknadGjelder")
        val behovsmeldingGjelder: String?,
        /**
         * Løs typing her, slik at vi slipper å oppdatere hm-soknadsbehandling hver gang
         * behovsmeldingsmodellen oppdaterer seg. hm-soknadsbehandling skal uansett bare videre sende data.
         * Typing av data i hm-soknad-api og hm-soknadsbehandling-db.
         */
        val behovsmeldingV2: Map<String, Any?>,
    ) : Behovsmeldingsgrunnlag {
        override val kilde: Kilde = Kilde.DIGITAL
        override val digital: Boolean = true
    }

    data class Papir(
        override val søknadId: UUID,
        override val status: BehovsmeldingStatus,
        override val fnrBruker: String,
        override val navnBruker: String,
        @JsonAlias("journalpostid")
        val journalpostId: String,
        val sakstilknytning: Sakstilknytning.Infotrygd? = null,
    ) : Behovsmeldingsgrunnlag {
        override val kilde: Kilde = Kilde.PAPIR
        override val digital: Boolean = false
    }

    /**
     * Grunnlaget for behovsmeldingen er journalpost mottatt i `hm-joark-listener`.
     *
     * Dette kan f.eks. være skannede papirsøknader eller søknader sendt inn med FyllUt-SendInn.
     */
    data class Journalpost(
        override val søknadId: UUID,
        override val status: BehovsmeldingStatus,
        override val fnrBruker: String,
        override val navnBruker: String,
        val journalpostId: String,
        val sakstilknytning: Sakstilknytning? = null,
        val kanal: Kanal? = null,
    ) : Behovsmeldingsgrunnlag {
        override val kilde: Kilde = Kilde.JOURNALPOST
        override val digital: Boolean = false
    }

    enum class Kilde {
        DIGITAL,
        PAPIR,
        JOURNALPOST,
    }
}
