package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.behovsmeldingsmodell.BehovsmeldingType
import no.nav.hjelpemidler.behovsmeldingsmodell.Brukerkilde
import no.nav.hjelpemidler.behovsmeldingsmodell.BrukersituasjonVilkårtype
import no.nav.hjelpemidler.behovsmeldingsmodell.BruksarenaV2
import no.nav.hjelpemidler.behovsmeldingsmodell.BytteÅrsak
import no.nav.hjelpemidler.behovsmeldingsmodell.FritakFraBegrunnelseÅrsak
import no.nav.hjelpemidler.behovsmeldingsmodell.Funksjonsnedsettelser
import no.nav.hjelpemidler.behovsmeldingsmodell.Hasteårsak
import no.nav.hjelpemidler.behovsmeldingsmodell.InnsenderRolle
import no.nav.hjelpemidler.behovsmeldingsmodell.KontaktpersonV2
import no.nav.hjelpemidler.behovsmeldingsmodell.LeveringTilleggsinfo
import no.nav.hjelpemidler.behovsmeldingsmodell.OppfølgingsansvarligV2
import no.nav.hjelpemidler.behovsmeldingsmodell.Prioritet
import no.nav.hjelpemidler.behovsmeldingsmodell.Signaturtype
import no.nav.hjelpemidler.behovsmeldingsmodell.UtleveringsmåteV2
import no.nav.hjelpemidler.behovsmeldingsmodell.UtlevertTypeV2
import no.nav.hjelpemidler.domain.geografi.Bydel
import no.nav.hjelpemidler.domain.geografi.Kommune
import no.nav.hjelpemidler.domain.geografi.Veiadresse
import no.nav.hjelpemidler.domain.artikkel.Artikkellinje
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.HarPersonnavn
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.TilknyttetPerson
import org.owasp.html.HtmlPolicyBuilder
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class Innsenderbehovsmelding(
    val bruker: Bruker,
    val brukersituasjon: Brukersituasjon,
    // Denne burde hete f.eks. "produkter" eller "artikler", siden den inneholder både hjelpemidler og tilbehør.
    val hjelpemidler: Hjelpemidler,
    val levering: Levering,
    val innsender: Innsender,
    val vedlegg: List<Vedlegg> = emptyList(),

    val metadata: InnsenderbehovsmeldingMetadata,
    val saksbehandlingvarsel: List<Varsel> = emptyList(),

    override val id: UUID,
    override val type: BehovsmeldingType,
    override val innsendingsdato: LocalDate,
    override val innsendingstidspunkt: Instant? = null,
    override val skjemaversjon: Int = 2,
    override val hjmBrukersFnr: Fødselsnummer = bruker.fnr,
    override val prioritet: Prioritet = tilPrioritet(levering.hast),
) : BehovsmeldingBase

data class Vedlegg(
    val id: UUID,
    val navn: String,
    val type: VedleggType,
)

enum class VedleggType {
    LEGEERKLÆRING_FOR_VARMEHJELPEMIDDEL,
}

data class InnsenderbehovsmeldingMetadata(
    val bestillingsordningsjekk: Bestillingsordningsjekk?,
)

data class Bruker(
    override val fnr: Fødselsnummer,
    val navn: Personnavn,
    val signaturtype: Signaturtype,
    val telefon: String?, // Inputfelt for innbyggers tlf fjernet: https://trello.com/c/FV6XBtrC/377-brukers-telefonnummer-i-digital-behovsmelding-hva-brukes-det-til
    val veiadresse: Veiadresse?,
    val kommunenummer: String?,
    val brukernummer: String?,
    val kilde: Brukerkilde?,
    val legacyopplysninger: List<EnkelOpplysning>, // for visning av opplysninger som bare finnes i eldre behovsmeldinger
) : TilknyttetPerson {
    val kildeErPdl: Boolean @JsonIgnore get() = kilde == Brukerkilde.PDL
}

data class Brukersituasjon(
    val vilkår: Set<BrukersituasjonVilkårV2>,
    val funksjonsnedsettelser: Set<Funksjonsnedsettelser>,
    val funksjonsbeskrivelse: Funksjonsbeskrivelse?,
)

data class BrukersituasjonVilkårV2(
    val vilkårtype: BrukersituasjonVilkårtype,
    val tekst: LokalisertTekst,
)

data class Levering(
    val hjelpemiddelformidler: Hjelpemiddelformidler,

    val oppfølgingsansvarlig: OppfølgingsansvarligV2,
    val annenOppfølgingsansvarlig: AnnenOppfølgingsansvarlig?,

    /**
     * utleveringsmåte == null -> formidler har ikke fått spm om utlevering fordi det ikke er behov for denne infoen.
     * Skjer når hvert hjm. er markert som utlevert eller ikke trenger info om utlevering (feks for apper hvor lisens
     * sendes til MinSide på nav.no, eller til folkereg. adresse for barn under 18 år).
     */
    val utleveringsmåte: UtleveringsmåteV2?,
    val annenUtleveringsadresse: Veiadresse?,
    val annenUtleveringskommune: Kommune? = null,
    val annenUtleveringsbydel: Bydel? = null,

    // utleveringKontaktperson == null => alle hjm. er allerede utlevert
    val utleveringKontaktperson: KontaktpersonV2?,
    val annenKontaktperson: AnnenKontaktperson?,

    val utleveringMerknad: String,

    val hast: Hast?,

    /**
     * Inneholder ekstra informasjon som automatisk er utledet. Dvs. det er ikke noe formidler har svart på (direkte).
     */
    val automatiskUtledetTilleggsinfo: Set<LeveringTilleggsinfo> = emptySet(),
) {
    val harFritekstUnderOppfølgingsansvarlig: Boolean
        @JsonIgnore
        get() = !annenOppfølgingsansvarlig?.ansvarFor.isNullOrBlank()

    val harFritekstUnderLevering: Boolean
        @JsonIgnore
        get() = utleveringMerknad.isNotBlank()

    val alleHjelpemidlerErAlleredeUtlevert: Boolean
        @JsonIgnore
        get() = utleveringsmåte == null || utleveringsmåte == UtleveringsmåteV2.ALLEREDE_UTLEVERT_AV_NAV

    data class Hjelpemiddelformidler(
        override val navn: Personnavn,
        val arbeidssted: String,
        val stilling: String,
        val telefon: String,
        val adresse: Veiadresse,
        val epost: String,
        val treffesEnklest: String,
        val kommunenavn: String?,
        val kommunenummer: String? = null,
    ) : HarPersonnavn

    data class AnnenOppfølgingsansvarlig(
        override val navn: Personnavn,
        val arbeidssted: String,
        val stilling: String,
        val telefon: String,
        val ansvarFor: String,
    ) : HarPersonnavn

    data class AnnenKontaktperson(
        override val navn: Personnavn,
        val telefon: String,
    ) : HarPersonnavn
}

data class Hast(
    val hasteårsaker: Set<Hasteårsak>,
    val hastBegrunnelse: String?,
)

data class Innsender(
    val rolle: InnsenderRolle,
    val erKommunaltAnsatt: Boolean?,
    val kurs: List<Godkjenningskurs>,
    val sjekketUtlånsoversiktForKategorier: Set<Iso6>?,
)

data class Hjelpemidler(
    val hjelpemidler: List<Hjelpemiddel>,
    val tilbehør: List<Tilbehør> = emptyList(),
    val totaltAntall: Int,
) : Iterable<Hjelpemiddel> by hjelpemidler {
    /**
     * Sett av alle [ArtikkelBase.hmsArtNr] fra alle hjelpemidler og tilbehør.
     */
    val hmsArtNrs: Set<String>
        @JsonIgnore
        get() = artikler.mapTo(sortedSetOf(), Artikkellinje::hmsArtNr)

    /**
     * Liste av alle artikler, både hjelpemidler med tilhørende tilbehør og frittstående tilbehør.
     */
    val artikler: List<Artikkellinje>
        @JsonIgnore
        get() = hjelpemidler.flatMap { listOf(it) + it.tilbehør } + tilbehør
}

data class Hjelpemiddel(
    /**
     * Tilfeldig generert id for å kunne unikt identifisere hjelpemidler,
     * f.eks. dersom det er lagt til flere innslag med samme hmsArtNr.
     * For gamle saker: `hjelpemiddelId = hjelpemiddel.produkt.stockid + new Date().getTime()`.
     * For nye saker (etter ca. 2024-11-05): `hjelpemiddelId = UUID()`.
     */
    val hjelpemiddelId: String,
    override val antall: Int,
    val produkt: HjelpemiddelProdukt,
    val tilbehør: List<Tilbehør>,
    val bytter: List<Bytte>,
    val bruksarenaer: Set<BruksarenaV2>,
    val utlevertinfo: Utlevertinfo,
    val opplysninger: List<Opplysning>,
    val varsler: List<Varsel>,
    val saksbehandlingvarsel: List<Varsel> = emptyList(),
) : Artikkellinje {
    override val id: String
        @JsonIgnore
        get() = hjelpemiddelId

    override val hmsArtNr: String
        @JsonIgnore
        get() = produkt.hmsArtNr

    override val artikkelnavn: String
        @JsonIgnore
        get() = produkt.artikkelnavn
}

data class Bytte(
    val erTilsvarende: Boolean,
    val hmsnr: String,
    val serienr: String? = null,
    val hjmNavn: String,
    val hjmKategori: String,
    val årsak: BytteÅrsak? = null,
    val versjon: String = "v1",
)

data class HjelpemiddelProdukt(
    val hmsArtNr: String,
    val artikkelnavn: String,
    val iso8: Iso8,
    val iso8Tittel: String,
    /**
     * Brukes blant annet til generering av dokumenttittel.
     * Defaulter til tom string for gamle saker sendt inn før feltet ble lagt til (ca 2026-01-09).
     */
    val iso8KortTittel: String = "",
    val delkontrakttittel: String,
    /**
     * Fra digihot-sortiment.
     */
    val sortimentkategori: String,
    /**
     * Brukt av hm-saksfordeling for å sortere til Gosys.
     */
    val delkontraktId: String?,
    /**
     * null -> ikke på rammeavtale
     * Har i sjeldne tilfeller skjedd at formidler får søkt om produkt som ikke lenger er på rammeavtale, antageligvis pga.
     * endring i produkter på rammeavtale etter lansering av rammeavtalen.
     */
    val rangering: Int?,
)

data class Tilbehør(
    val tilbehørId: UUID? = null,
    override val hmsArtNr: String,
    val navn: String,
    /**
     * Brukes blant annet til generering av dokumenttittel.
     * Defaulter til null for gamle saker sendt inn før feltet ble lagt til (ca 2026-01-13).
     */
    val iso6: Iso6? = null,
    override val antall: Int,
    val begrunnelse: String?,
    val fritakFraBegrunnelseÅrsak: FritakFraBegrunnelseÅrsak?,
    val opplysninger: List<Opplysning> = emptyList(),
    val saksbehandlingvarsel: List<Varsel> = emptyList(),
) : Artikkellinje {
    override val id: String
        @JsonIgnore
        get() = tilbehørId?.toString() ?: ""

    override val artikkelnavn: String
        @JsonIgnore
        get() = navn
}

data class Utlevertinfo(
    val alleredeUtlevertFraHjelpemiddelsentralen: Boolean,
    val utleverttype: UtlevertTypeV2?,
    val overførtFraBruker: Brukernummer?,
    val annenKommentar: String?,
)

data class Godkjenningskurs(
    val id: Int,
    val title: String,
    val kilde: String,
)

data class Funksjonsbeskrivelse(
    val innbyggersVarigeFunksjonsnedsettelse: InnbyggersVarigeFunksjonsnedsettelse,
    val diagnose: String?,
    val beskrivelse: String,
)

enum class InnbyggersVarigeFunksjonsnedsettelse {
    ALDERDOMSSVEKKELSE,
    ANNEN_VARIG_DIAGNOSE,
    ANNEN_DIAGNOSE,
    UAVKLART,
    UAVKLART_V2,
}

typealias Brukernummer = String

data class Opplysning(
    val ledetekst: LokalisertTekst,
    val innhold: List<Tekst>,
) {
    constructor(ledetekst: LokalisertTekst, innhold: Tekst) : this(ledetekst = ledetekst, innhold = listOf(innhold))

    constructor(ledetekst: LokalisertTekst, innhold: String) : this(ledetekst = ledetekst, innhold = Tekst(innhold))

    constructor(ledetekst: LokalisertTekst, innhold: LokalisertTekst) : this(
        ledetekst = ledetekst,
        innhold = Tekst(innhold),
    )

    init {
        sanitize(ledetekst)
    }
}

data class EnkelOpplysning(
    val ledetekst: LokalisertTekst,
    val innhold: LokalisertTekst,
)

data class Tekst(
    val fritekst: String? = null,
    val forhåndsdefinertTekst: LokalisertTekst? = null,
    val begrepsforklaring: LokalisertTekst? = null, // feks forklaring av "avlastningsbolig". Ikke relevant for fritekst.
) {
    constructor(forhåndsdefinertTekst: LokalisertTekst) : this(
        forhåndsdefinertTekst = forhåndsdefinertTekst,
        fritekst = null,
    )

    constructor(fritekst: String) : this(forhåndsdefinertTekst = null, fritekst = fritekst)
    constructor(nb: String, nn: String) : this(LokalisertTekst(nb = nb, nn = nn))

    init {
        require(
            (forhåndsdefinertTekst != null && fritekst == null) ||
                    (forhåndsdefinertTekst == null && fritekst != null),
        ) { "Én, og bare én, av forhåndsdefinertTekst eller fritekst må ha verdi. Mottok forhåndsdefinertTekst <$forhåndsdefinertTekst> og fritekst <$fritekst>" }

        sanitize(begrepsforklaring)
        sanitize(forhåndsdefinertTekst)
    }
}

data class LokalisertTekst(
    val nb: String,
    val nn: String,
) {
    constructor(norsk: String) : this(nb = norsk, nn = norsk) // For enkle tekster som er like på begge målformer
}

data class Varsel(
    val tekst: LokalisertTekst,
    val type: Varseltype,
)

enum class Varseltype {
    INFO,
    WARNING,
}

private fun tilPrioritet(hast: Hast?): Prioritet = if (hast != null) Prioritet.HAST else Prioritet.NORMAL

private val htmlPolicy = HtmlPolicyBuilder().allowElements("em", "strong").toFactory()

private fun sanitize(tekst: LokalisertTekst?) {
    if (tekst == null) return
    require(tekst.nb == htmlPolicy.sanitize(tekst.nb)) { "Ugyldig HTML i nb" }
    require(tekst.nn == htmlPolicy.sanitize(tekst.nn)) { "Ugyldig HTML i nn" }
}
