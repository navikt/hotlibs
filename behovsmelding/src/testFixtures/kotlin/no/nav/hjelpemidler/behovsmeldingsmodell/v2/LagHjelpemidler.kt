package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import java.util.UUID

fun lagHjelpemiddel(hmsArtNr: String, antall: Int = 1, tilbehør: List<Tilbehør> = emptyList()) = Hjelpemiddel(
    hjelpemiddelId = UUID.randomUUID().toString(),
    antall = antall,
    produkt = HjelpemiddelProdukt(
        hmsArtNr, "Artikkelnavn for $hmsArtNr",
        iso8 = Iso8("12345678"),
        iso8Tittel = "",
        delkontrakttittel = "",
        sortimentkategori = "",
        delkontraktId = null,
        rangering = 1,
    ),
    tilbehør = tilbehør,
    bytter = emptyList(),
    bruksarenaer = emptySet(),
    utlevertinfo = Utlevertinfo(
        alleredeUtlevertFraHjelpemiddelsentralen = false,
        utleverttype = null,
        overførtFraBruker = null,
        annenKommentar = null,
    ),
    opplysninger = emptyList(),
    varsler = emptyList(),
    saksbehandlingvarsel = emptyList(),
)

fun lagTilbehør(hmsArtNr: String, antall: Int = 1) = Tilbehør(
    tilbehørId = UUID.randomUUID(),
    hmsArtNr = hmsArtNr,
    navn = "Navn for $hmsArtNr",
    antall = antall,
    begrunnelse = null,
    fritakFraBegrunnelseÅrsak = null,
    opplysninger = emptyList(),
    saksbehandlingvarsel = emptyList(),
)
