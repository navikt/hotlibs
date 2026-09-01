package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import no.nav.hjelpemidler.behovsmeldingsmodell.BehovsmeldingType
import no.nav.hjelpemidler.behovsmeldingsmodell.InnsenderRolle
import no.nav.hjelpemidler.behovsmeldingsmodell.OppfølgingsansvarligV2
import no.nav.hjelpemidler.behovsmeldingsmodell.Signaturtype
import no.nav.hjelpemidler.domain.geografi.Veiadresse
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.Personnavn
import no.nav.hjelpemidler.domain.person.år
import java.time.LocalDate
import java.util.UUID

fun lagInnsenderbehovsmelding(
    hjelpemidler: Hjelpemidler = Hjelpemidler(hjelpemidler = emptyList(), totaltAntall = 0),
    vedlegg: List<Vedlegg> = emptyList(),
) = Innsenderbehovsmelding(
    bruker = Bruker(
        fnr = Fødselsnummer(40.år),
        navn = Personnavn(fornavn = "Fornavn", etternavn = "Etternavn"),
        signaturtype = Signaturtype.BRUKER_BEKREFTER,
        telefon = null,
        veiadresse = null,
        kommunenummer = null,
        brukernummer = null,
        kilde = null,
        legacyopplysninger = emptyList(),
    ),
    brukersituasjon = Brukersituasjon(
        vilkår = emptySet(),
        funksjonsnedsettelser = emptySet(),
        funksjonsbeskrivelse = null,
    ),
    hjelpemidler = hjelpemidler,
    levering = Levering(
        hjelpemiddelformidler = Levering.Hjelpemiddelformidler(
            navn = Personnavn(fornavn = "Formidler", etternavn = "Formidlersen"),
            arbeidssted = "Arbeidssted",
            stilling = "Stilling",
            telefon = "12345678",
            adresse = Veiadresse("Gate 1", "0001", "Oslo"),
            epost = "formidler@nav.no",
            treffesEnklest = "På dagtid",
            kommunenavn = "Oslo",
        ),
        oppfølgingsansvarlig = OppfølgingsansvarligV2.HJELPEMIDDELFORMIDLER,
        annenOppfølgingsansvarlig = null,
        utleveringsmåte = null,
        annenUtleveringsadresse = null,
        utleveringKontaktperson = null,
        annenKontaktperson = null,
        utleveringMerknad = "",
        hast = null,
    ),
    innsender = Innsender(
        rolle = InnsenderRolle.FORMIDLER,
        erKommunaltAnsatt = null,
        kurs = emptyList(),
        sjekketUtlånsoversiktForKategorier = null,
    ),
    vedlegg = vedlegg,
    metadata = InnsenderbehovsmeldingMetadata(bestillingsordningsjekk = null),
    id = UUID.randomUUID(),
    type = BehovsmeldingType.SØKNAD,
    innsendingsdato = LocalDate.now(),
)

fun lagProduktkategori(
    vedlegg: List<Vedlegg> = emptyList(),
    komponenter: List<ProduktkategoriKomponent> = emptyList(),
) = Produktkategori(
    id = UUID.randomUUID(),
    type = ProduktkategoriType.DØRAUTOMATIKK,
    navn = "Dørautomatikk",
    antall = 1,
    delkontrakttittel = "Dørautomatikk",
    bruksarenaer = emptyList(),
    opplysninger = emptyList(),
    vedlegg = vedlegg,
    komponenter = komponenter,
)

fun lagProduktkategoriKomponent(vedlegg: List<Vedlegg> = emptyList()) = ProduktkategoriKomponent(
    id = UUID.randomUUID().toString(),
    type = ProduktkategoriKomponentType.DØR,
    navn = "Dør 1",
    opplysninger = emptyList(),
    vedlegg = vedlegg,
)

fun lagVedlegg(type: VedleggType = VedleggType.LEGEERKLÆRING_FOR_VARMEHJELPEMIDDEL) = Vedlegg(
    id = UUID.randomUUID(),
    navn = type.name,
    type = type,
)
