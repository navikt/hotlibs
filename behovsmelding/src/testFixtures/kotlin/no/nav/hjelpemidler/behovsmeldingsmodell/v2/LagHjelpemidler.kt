package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import java.util.UUID

fun lagHjelpemidler(): Hjelpemidler {
    val hjelpemidler = listOf(
        lagHjelpemiddel("100001", 1),
        lagHjelpemiddel("100002", 1),
    )
    val tilbehør = listOf(
        lagTilbehør("300001", 1),
        lagTilbehør("300002", 2),
    )
    return Hjelpemidler(
        hjelpemidler = hjelpemidler,
        tilbehør = tilbehør,
        totaltAntall = hjelpemidler
            .sumOf { hjelpemiddel -> hjelpemiddel.antall + tilbehør.sumOf(ArtikkelBase::antall) }
                + tilbehør.sumOf(ArtikkelBase::antall),
    )
}

fun lagHjelpemiddel(hmsArtNr: String, antall: Int = 1) = Hjelpemiddel(
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
    tilbehør = listOf(
        lagTilbehør("200001", 1),
        lagTilbehør("200002", 2),
    ),
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
